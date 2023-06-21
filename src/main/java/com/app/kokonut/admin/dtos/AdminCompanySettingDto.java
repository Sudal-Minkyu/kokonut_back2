package com.app.kokonut.admin.dtos;

import com.app.kokonut.admin.enums.AuthorityRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-06-13
 * Time :
 * Remark : 사용자 비밀번호 오류횟수 초과용 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCompanySettingDto {

    private Integer knPwdErrorCount;

    private String csPasswordErrorCountSetting;

    private AuthorityRole knRoleCode;

    public String getKnRoleCode() {
        return knRoleCode.getCode();
    }

}
