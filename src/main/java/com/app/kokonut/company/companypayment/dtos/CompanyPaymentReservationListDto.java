package com.app.kokonut.company.companypayment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-06-20
 * Time :
 * Remark : 결제 예약할 테이블명 리스트
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyPaymentReservationListDto {

    private String cpCode; // 회사코드

    private String ctName; // 조회된 테이블명

    private String cpiPayType; // 결제타입 - '0' : 월 정기구독, '1' : 연 정기구독

    private String cpiBillingKey; // 카드(빌링키)

    private LocalDate cpiValidStart; // 자동결제부과 시작일

    private Long cpiId;

    private String subscribeCheck; // 구독해지 여부 : "1" 일 경우 구독해지상태, "2"일 경우 정상

    private LocalDateTime cpSubscribeDate;
}
