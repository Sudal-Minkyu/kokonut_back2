package com.app.kokonut.privacy.policy.dtos;

import com.app.kokonut.privacy.policy.policyout.dtos.PolicyOutSaveDto;
import com.app.kokonut.privacy.policy.policyoutdetail.dtos.PolicyOutDetailSaveDto;
import com.app.kokonut.privacy.policy.policypurpose.dtos.PolicyPurposeSaveDto;
import lombok.Data;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 개인정보처리방침 제작 네번째 뎁스 데이터받는 Dto
 */
@Data
public class PolicySaveFourthDto {

    private Long piId; // 개인정보처리방침 ID

    private List<PolicyOutSaveDto> policyOutSaveDtoList; // 처리업무의 위탁에 관한사항 저장 리스트값

    private List<Long> policyOutDeleteIdList; // 처리업무의 위탁에 관한사항 삭제 Id 리스트값

    private Integer policyOutDetailYn; // 0: 미포함, 1: 포함

    private List<PolicyOutDetailSaveDto> policyOutDetailSaveDtoList; // 처리업무의 국외 위탁에 관한사항 저장 리스트값

    private List<Long> policyOutDetailDeleteIdList; // 처리업무의 국외 위탁에 관한사항 삭제 Id 리스트값

}
