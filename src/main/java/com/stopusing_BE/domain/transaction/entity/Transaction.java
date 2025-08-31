package com.stopusing_BE.domain.transaction.entity;

import com.stopusing_BE.domain.category.entity.Category;
import com.stopusing_BE.domain.user.entity.User;
import com.stopusing_BE.global.common.base.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transaction")
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
  private TransactionType type;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id")
  private User user;

  @Builder.Default
  @ManyToMany
  @JoinTable(
      name = "transaction_category_map",
      joinColumns = @JoinColumn(name = "transaction_id"),
      inverseJoinColumns = @JoinColumn(name = "category_id"),
      uniqueConstraints = @UniqueConstraint(
          name = "uk_tx_cat",
          columnNames = {"transaction_id","category_id"}
      )
  )
  private Set<Category> categories = new HashSet<>();

  // 편의 메서드
  public void addCategory(Category c) {
    categories.add(c);
    c.getTransactions().add(this);
  }
  public void removeCategory(Category c) {
    categories.remove(c);
    c.getTransactions().remove(this);
  }
  public void replaceCategories(Collection<Category> newOnes) {
    for (Category c : new HashSet<>(categories)) removeCategory(c);
    if (newOnes != null) for (Category c : newOnes) addCategory(c);
  }

}