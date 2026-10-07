# syntax=docker/dockerfile:1

# Step 1: Build
FROM gradle:8.14-jdk17-alpine AS builder

# The official Gradle image runs as the "gradle" user, which can't write to /app.
# Run as root in this throwaway build stage and point Gradle's cache at a known path.
USER root
ENV GRADLE_USER_HOME=/root/.gradle

WORKDIR /app

# Copy Gradle config files first
COPY build.gradle* settings.gradle* gradle.properties* ./
COPY gradle gradle
COPY gradlew .

# Download dependencies into the persistent cache (same mount as the build step)
RUN --mount=type=cache,target=/root/.gradle \
    gradle dependencies --no-daemon --stacktrace

# Copy source code
COPY src ./src

# Build the executable Spring Boot jar, reusing the same cache
RUN --mount=type=cache,target=/root/.gradle \
    gradle clean bootJar -x test --no-daemon --stacktrace

# Step 2: Runtime
FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="reaksa"
LABEL description="Spring Boot Demo Application"
LABEL version="0.0.1-SNAPSHOT"

# Create a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

# Copy the built jar and set ownership in one layer
COPY --from=builder --chown=spring:spring /app/build/libs/*.jar app.jar

USER spring

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]