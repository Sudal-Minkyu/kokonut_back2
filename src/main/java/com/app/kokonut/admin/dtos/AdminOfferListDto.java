package com.app.kokonut.admin.dtos;

import com.app.kokonut.admin.enums.AuthorityRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-05-03
 * Time :
 * Remark : 개인정보 내부제공, 외부제공 할 명단 ListDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminOfferListDto {

    private Long adminId;

    private String knEmail;

    private String knName;

    private String knDepartment;

    private AuthorityRole knRoleCode;

    private AuthorityRole knRoleDesc;

    public String getKnRoleDesc() {
        return knRoleDesc.getDesc();
    }

    public String getKnRoleCode() {
        return knRoleCode.getCode();
    }

}
