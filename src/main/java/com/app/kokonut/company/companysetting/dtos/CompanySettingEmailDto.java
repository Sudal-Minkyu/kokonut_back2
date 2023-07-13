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

    private String cpName; // 회사명

    private String csEmailCodeSetting; // 이메일발송 지정코드

}
