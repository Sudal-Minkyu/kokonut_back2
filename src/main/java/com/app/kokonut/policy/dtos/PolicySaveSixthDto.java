package com.app.kokonut.policy.dtos;

import com.app.kokonut.policy.policyresponsible.dtos.PolicyResponsibleSaveDto;
import lombok.Data;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 개인정보처리방침 제작 여섯번째 뎁스 데이터받는 Dto
 */
@Data
public class PolicySaveSixthDto {

    private Long piId; // 개인정보처리방침 ID

    private List<PolicyResponsibleSaveDto> policyResponsibleSaveDtoList; // 책임자 저장 리스트값

    private List<Long> policyResponsibleDeleteIdList; // 책임자 삭제 Id 리스트값

    private Boolean piChangeChose; // 개인정보 처리방침의 변경에 관한 사항 0: 미포함, 1: 포함 -> 체크박스여부임

    private String piYear; // 시행일자 년

    private String piMonth; // 시행일자 월

    private String piDay; // 시행일자 일

}
