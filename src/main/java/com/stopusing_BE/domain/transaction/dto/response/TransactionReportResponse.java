package com.stopusing_BE.domain.transaction.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionReportResponse {
  private Long totalPrice;
  private Long totalCount;
  private LocalDateTime startAt;
  private LocalDateTime endAt;
}
