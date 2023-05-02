package com.app.kokonut.policy.dtos;

import com.app.kokonut.policy.policyafter.dtos.PolicyAfterSaveDto;
import com.app.kokonut.policy.policybefore.dtos.PolicyBeforeSaveDto;
import com.app.kokonut.policy.policyserviceauto.dtos.PolicyServiceAutoSaveDto;
import lombok.Data;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 개인정보처리방침 제작 세번째 뎁스 데이터받는 Dto
 */
@Data
public class PolicySaveThirdDto {

    private Long piId; // 개인정보처리방침 ID

    private Boolean piInternetChose; // 인터넷접속로그 여부 0: 미선택, 1: 선택

    private Boolean piContractChose; // 계약또는청약철회 여부 0: 미선택, 1: 선택

    private Boolean piPayChose; // 대금결제 및 재화 여부 0: 미선택, 1: 선택

    private Boolean piConsumerChose; // 소피자의 불만 또는 분쟁처리 여부: 0: 미선택, 1: 선택

    private Boolean piAdvertisementChose; // 표시광고 0: 미선택, 1: 선택

    private List<PolicyBeforeSaveDto> policyBeforeSaveDtoList; // 서비스가입 시 수집하는 개인정보 저장 리스트값

    private List<Long> policyBeforeDeleteIdList; // 서비스가입 시 삭제 Id 리스트값

    private List<PolicyAfterSaveDto> policyAfterSaveDtoList; // 서비스가입 후 수집하는 개인정보 저장 리스트값

    private List<Long> policyAfterDeleteIdList; // 서비스가입 후 삭제 Id 리스트값

    private List<PolicyServiceAutoSaveDto> policyServiceAutoSaveDtoList; // 서비스이용중 자동으로 수집하는 개인정보 저장 리스트값

    private List<Long> policyServiceAutoDeleteIdList; // 서비스이용중 자동수집 삭제 Id 리스트값


}
