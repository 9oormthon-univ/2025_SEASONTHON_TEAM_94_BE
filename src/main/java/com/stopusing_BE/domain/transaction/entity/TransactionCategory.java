package com.stopusing_BE.domain.transaction.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionCategory {
  FOOD("식비"),
  GROCERIES("식료품/마트"),
  TRANSPORT("교통"),
  CAR("자동차"),
  HOUSING("주거"),
  UTILITIES("공과금"),
  TELECOM("통신"),
  SUBSCRIPTIONS("구독/멤버십"),
  SHOPPING("쇼핑/의류"),
  BEAUTY("미용/뷰티"),
  HEALTHCARE("의료/건강"),
  EDUCATION("교육/자기계발"),
  ENTERTAINMENT("취미/여가/영화"),
  TRAVEL("여행"),
  PETS("반려동물"),
  GIFTS_OCCASIONS("경조사/선물"),
  INSURANCE("보험"),
  TAXES_FEES("세금/수수료"),
  DONATION("기부"),
  OTHER("기타");

  private final String label; // 사람이 읽는 라벨
}