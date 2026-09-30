package com.example.demo.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.jsontype.PolymorphicTypeValidator;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class RedisConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // build JSON serializer for redis
        GenericJacksonJsonRedisSerializer jsonRedisSerializer = this.buildRedisJsonSerializer();

        // default configuration
        RedisCacheConfiguration defaultConfig = this.getRedisCacheConfig(30, jsonRedisSerializer);

        // pagination configuration
        RedisCacheConfiguration paginatedConfig = this.getRedisCacheConfig(5, jsonRedisSerializer);

        // paginated cache with 5mins TTL
        Map<String, RedisCacheConfiguration> paginationCacheConfigs = new HashMap<>();

        paginationCacheConfigs.put("products-paginated", paginatedConfig);

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(paginationCacheConfigs)
                .build();
    }

    private GenericJacksonJsonRedisSerializer buildRedisJsonSerializer() {
        PolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(Object.class)
                .build();

        // Jackson 3 mappers are immutable: configure everything on the builder.
        // java.time support is built in, so no JavaTimeModule is needed.
        JsonMapper jsonMapper = JsonMapper.builder()
                .changeDefaultVisibility(vc ->
                        vc.withVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY))
//                .activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL)
                .build();

        return new GenericJacksonJsonRedisSerializer(jsonMapper);
    }

    private RedisCacheConfiguration  getRedisCacheConfig(Integer duration, GenericJacksonJsonRedisSerializer jsonRedisSerializer) {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(duration))
                .computePrefixWith(cacheName -> cacheName + ":")
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
                )
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(jsonRedisSerializer)
                )
                .disableCachingNullValues();
    }
}
