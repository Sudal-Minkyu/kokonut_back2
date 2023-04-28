package com.app.kokonut.privacy.policy.policypurpose.dtos;

import lombok.Data;

/**
 * @author Woody
 * Date : 2023-04-21
 * Time :
 * Remark : 개인정보처리방침의 개인정보처리목적 두번째 뎁스 저장 데이터받는 Dto
 */
@Data
public class PolicyPurposeSaveDto {

    private Long pipId;

    private String pipTitle; // 처리목적 제목

    private String pipContent; // 처리목적 내용

}
