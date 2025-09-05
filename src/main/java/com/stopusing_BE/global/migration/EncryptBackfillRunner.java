package com.stopusing_BE.global.migration;

import com.stopusing_BE.domain.transaction.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("migrate") // 이 프로필에서만 실행
@RequiredArgsConstructor
public class EncryptBackfillRunner implements CommandLineRunner {

  private final JdbcTemplate jdbcTemplate;
  private final EncryptUtil encryptUtil;

  private static final int BATCH_SIZE = 1000;

  @Override
  public void run(String... args) {
    long lastId = 0L;

    while (true) {
      var rows = jdbcTemplate.queryForList(
          "SELECT id, title, bank_name, memo FROM `transaction` " +
              "WHERE id > ? ORDER BY id ASC LIMIT ?",
          lastId, BATCH_SIZE
      );
      if (rows.isEmpty()) break;

      for (var row : rows) {
        Long id = ((Number) row.get("id")).longValue();
        String title = (String) row.get("title");
        String bank  = (String) row.get("bank_name");
        String memo  = (String) row.get("memo");

        // 필요시 '이미 암호문으로 보이면 스킵' 로직 추가 가능(Base64 패턴 검사 등)
        String encTitle = (title != null) ? encryptUtil.encryptContent(title) : null;
        String encBank  = (bank  != null) ? encryptUtil.encryptContent(bank)  : null;
        String encMemo  = (memo  != null) ? encryptUtil.encryptContent(memo)  : null;

        jdbcTemplate.update(
            "UPDATE `transaction` SET title=?, bank_name=?, memo=? WHERE id=?",
            encTitle, encBank, encMemo, id
        );
        lastId = id;
      }
    }
    System.out.println("Backfill finished.");
  }
}
