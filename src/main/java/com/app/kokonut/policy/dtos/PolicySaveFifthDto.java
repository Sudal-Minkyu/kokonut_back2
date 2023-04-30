package com.app.kokonut.policy.dtos;

import com.app.kokonut.policy.policythird.dtos.PolicyThirdSaveDto;
import com.app.kokonut.policy.policythirdoverseas.dtos.PolicyThirdOverseasSaveDto;
import lombok.Data;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 개인정보처리방침 제작 다섯번째 뎁스 데이터받는 Dto
 */
@Data
public class PolicySaveFifthDto {

    private Long piId; // 개인정보처리방침 ID

    private Integer policyThirdYn; // 제3자 제공에 관한 사항 체크박스여부 0: 미포함, 1: 포함

    private List<PolicyThirdSaveDto> policyThirdSaveDtoList; //  제3자 제공에 관한 사항 저장 리스트값

    private List<Long> policyThirdDeleteIdList; // 제3자 제공에 관한 사항 삭제 Id 리스트값

    private Integer policyThirdOverseasYn; // 국외 제3자 제공에 관한 사항 체크박스 여부 0: 미포함, 1: 포함 -> 체크박스여부임

    private List<PolicyThirdOverseasSaveDto> policyThirdOverseasSaveDtoList; // 국외 제3자 제공에 관한 사항 저장 리스트값

    private List<Long> policyThirdOverseasDeleteIdList; // 국외 제3자 제공에 관한 사항 삭제 Id 리스트값

}
