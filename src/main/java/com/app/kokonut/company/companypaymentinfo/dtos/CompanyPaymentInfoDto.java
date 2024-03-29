package com.app.kokonut.company.companypaymentinfo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-06-14
 * Time :
 * Remark : 빌링 저장데이터 받는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyPaymentInfoDto {

    private String cpiPayType; // 결제타입 - '0' : 월 정기구독, '1' : 연 정기구독

    private String cpiInfoCardName; // 카드사명(부트페이제공 데이터)

}
