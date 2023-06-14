package com.app.kokonut.company.companypayment.dtos;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-06-14
 * Time :
 * Remark : 빌링 저장데이터 받는 Dto
 */
@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class CompanyPaymentSaveDto {

    // CompanyPayment
    private String cpiBillingKey; // 카드(빌링키)

    private LocalDateTime cpiBillingDate; // 빌링키 발급 일자(부트페이제공 데이터)

    private LocalDateTime cpiBillingExpireDate; // 빌링키 만료 일자 (부트페이제공 데이터)

    private String cpiReceiptId; // 빌링키 발급에 대한 부트페이 고유 영수증 ID

    private String cpiSubscriptionId; // 자동결제 고유ID(부트페이제공 데이터)

    // CompanyPaymentInfo
    private String cpiInfoCardName; // 카드사명(부트페이제공 데이터)

    private String cpiInfoCardNo; // 마스킹처리된 카드번호(부트페이제공 데이터)

    private String cpiInfoCardType; // 카드종류 0:신용카드, 1:체크카드(부트페이제공 데이터)

}
