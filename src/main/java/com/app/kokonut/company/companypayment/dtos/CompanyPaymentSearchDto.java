package com.app.kokonut.company.companypayment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-06-19
 * Time :
 * Remark : 카드 빌링키 정보 조회 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyPaymentSearchDto {

    private String cpiBillingKey; // 카드(빌링키)

    private String knName;

    private String knPhoneNumber;

}
