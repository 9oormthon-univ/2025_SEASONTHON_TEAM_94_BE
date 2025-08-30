package com.stopusing_BE.global.common.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {

  @Column(columnDefinition = "TIMESTAMP(6)", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdAt;

  @Column(columnDefinition = "TIMESTAMP(6)", nullable = false)
  @LastModifiedDate
  protected LocalDateTime updatedAt;


}