package com.example.demo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    @Column(name = "created_by", updatable = false, nullable = false)
    private String createdBy;

    @Column(name = "created_on", updatable = false, nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "modified_by", insertable = false)
    private String modifiedBy;

    @Column(name = "modified_on", insertable = false)
    private LocalDateTime modifiedOn;

    public void markCreated(String user) {
        this.createdBy = user;
        this.createdOn = LocalDateTime.now();
    }

    public void markModified(String user) {
        this.modifiedBy = user;
        this.modifiedOn = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        this.createdOn = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedOn = LocalDateTime.now();
    }
}
