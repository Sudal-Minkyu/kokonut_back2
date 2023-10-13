package com.app.kokonut.admin.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-03-29
 * Time :
 * Remark : 관리자 목록관리에 보여줄 ListDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminListDto {

    private String knName;

    private String knEmail;

    private String knRoleCode;

    private String knRoleDesc;

    private String insertName;

    private String insert_date;

    private String knLastLoginDate;

    private String knIpAddr;

    private String knIsEmailAuth; // 이메일 인증여부

    private String knActiveStatus; // 상태

}
