package com.stopusing_BE.domain.transaction.entity;

import com.stopusing_BE.domain.user.entity.User;
import com.stopusing_BE.global.common.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "`transaction`") // 백틱으로 인용
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Transaction extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "started_at", columnDefinition = "TIMESTAMP(6)", nullable = false)
  private LocalDateTime startedAt;

  @Column(nullable = false)
  private Long price;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TransactionCategory category;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TransactionType type;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_uid")
  private User user;

}