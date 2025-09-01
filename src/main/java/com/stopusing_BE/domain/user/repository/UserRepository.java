package com.stopusing_BE.domain.user.repository;

import com.stopusing_BE.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findUserByUsername(String username);
  Optional<User> findByUid(String uid);
}
