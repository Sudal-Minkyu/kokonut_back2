package com.app.kokonut.privacy.policy.policyafter.dtos;

import com.app.kokonut.privacy.policy.policythird.dtos.PolicyThirdSaveDto;
import com.app.kokonut.privacy.policy.policythirdoverseas.dtos.PolicyThirdOverseasSaveDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 개인정보처리방침의 가입 후 수집하는 개인정보 세번째 뎁스 저장 리스트 데이터받는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyAfterSaveInfoListDto {

    private Long piaId;

    private String piaPurpose; // 가입후 처리목적

    private String piaInfo; // 수집항목

    private String piaChose; // 필수/선택

    private String piaPeriod; // 처리및보유기간

}
