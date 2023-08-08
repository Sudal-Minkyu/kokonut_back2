package com.app.kokonut.company.companypayment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-06-17
 * Time :
 * Remark : 일일 개인정보 수를 집계할 테이블명 리스트
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyPaymentListDto {

    private String cpCode; // 회사코드

    private String ctName; // 조회된 테이블명

}
