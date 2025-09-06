package com.stopusing_BE.domain.transaction.dto.response;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionCalendarResponse {
  Map<Long, Long> totals;
}
