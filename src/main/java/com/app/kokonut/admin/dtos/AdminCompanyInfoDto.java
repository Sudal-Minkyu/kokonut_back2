package com.app.kokonut.admin.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2022-12-15
 * Time :
 * Remark : 사업자 정보 단일 조회용 Dto -> 추후 추가될 수 있음
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCompanyInfoDto {

    private Long adminId;

    private Long companyId;

    private String companyCode; // cpCode

}
