package com.app.kokonut.policy.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-04-30
 * Time :
 * Remark : 개인정보처리방침 제작 여섯 뎁스 저장된 데이터받는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyThirdInfoDto {

    private String piYear; // 시행일자 년

    private String piMonth; // 시행일자 월

    private String piDay; // 시행일자 일


}
