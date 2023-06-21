package com.app.kokonut.company.companypayment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
