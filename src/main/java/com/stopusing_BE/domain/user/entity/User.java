package com.stopusing_BE.domain.user.entity;

import com.github.f4b6a3.ulid.UlidCreator;
import com.stopusing_BE.domain.transaction.entity.Transaction;
import com.stopusing_BE.global.common.base.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

  @Id
  @Column(name = "uid", nullable = false, length = 26, unique = true)
  @Builder.Default
  private String uid = UlidCreator.getUlid().toString();
  private String username;
  private String nickname;
  private String role;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Transaction> transactions = new ArrayList<>();

}

