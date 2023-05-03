package com.app.kokonut.policy.dtos;

import lombok.Data;

/**
 * @author Woody
 * Date : 2023-04-21
 * Time :
 * Remark : 개인정보처리방침 제작 첫번째 뎁스 데이터받는 Dto
 */
@Data
public class PolicySaveFirstDto {

    private Long piId; // -> 신규일 경우 "0"을 보내주면됨

    private String piVersion; // 개정본 버전

    private String piDate; // 개정일

    private String piHeader; // 머리말 기업명

}
