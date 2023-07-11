package com.app.kokonut.company.companysetting.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-07-07
 * Time :
 * Remark : 이메일 셋팅값 가져오는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanySettingEmailDto {

    private String csEmailTableSetting; // 이메일발송 지정 테이블

    private String csEmailCodeSetting; // 이메일발송 지정코드

}
