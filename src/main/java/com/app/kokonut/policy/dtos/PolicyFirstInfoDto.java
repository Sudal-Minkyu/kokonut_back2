package com.app.kokonut.policy.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-04-25
 * Time :
 * Remark : 개인정보처리방침 제작 첫번째 뎁스 저장된 데이터받는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyFirstInfoDto {

    private Double piVersion; // 개정본 버전

    private String piDate; // 개정일

    private String piHeader; // 머리말 기업명

}
