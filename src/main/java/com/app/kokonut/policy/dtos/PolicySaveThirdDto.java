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

    private String piChoseListString; // 기본베이스 법령에 따른 개인정보의 보유기간 선택리스트

    private List<piChoseCustomDto> piChoseCustomList; // 개인정보처리방침 ID

    private List<Long> piChoseCustomDeleteIdList; // 개인정보보유기간 삭제 ID 리스트

    private List<PolicyBeforeSaveDto> policyBeforeSaveDtoList; // 서비스가입 시 수집하는 개인정보 저장 리스트값

    private List<Long> policyBeforeDeleteIdList; // 서비스가입 시 삭제 Id 리스트값

    private List<PolicyAfterSaveDto> policyAfterSaveDtoList; // 서비스가입 후 수집하는 개인정보 저장 리스트값

    private List<Long> policyAfterDeleteIdList; // 서비스가입 후 삭제 Id 리스트값

    private List<PolicyServiceAutoSaveDto> policyServiceAutoSaveDtoList; // 서비스이용중 자동으로 수집하는 개인정보 저장 리스트값

    private List<Long> policyServiceAutoDeleteIdList; // 서비스이용중 자동수집 삭제 Id 리스트값


}
