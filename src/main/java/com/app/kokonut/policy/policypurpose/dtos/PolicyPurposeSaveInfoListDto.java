package com.app.kokonut.policy.policypurpose.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-04-25
 * Time :
 * Remark : 개인정보처리방침의 개인정보처리목적 두번째 뎁스 저장 리스트 데이터받는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyPurposeSaveInfoListDto {

    private Long pipId;

    private String pipTitle; // 처리목적 제목

    private String pipContent; // 처리목적 내용

}
