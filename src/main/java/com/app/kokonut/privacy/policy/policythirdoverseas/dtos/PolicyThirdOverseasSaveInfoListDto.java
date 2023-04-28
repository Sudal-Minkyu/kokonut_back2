package com.app.kokonut.privacy.policy.policythirdoverseas.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 개인정보처리방침의 국외 제3자 제공에 관한 사항 다섯번째 뎁스 저장 리스트 데이터받는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyThirdOverseasSaveInfoListDto {

    private Long pitoId;

    private String pitoRecipient; // 제공받는자

    private String pitoLocation; // 제공받는자의 위치

    private String pitoPurpose; // 제공받는자의 목적

    private String pitoInfo; // 제공받는자의 항목

    private String pitoPeriod; // 제공받는자의 개인정보 보유 및 이용기간

}
