package com.app.kokonut.index.dtos;

import com.app.kokonut.admin.enums.AuthorityRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-06-26
 * Time :
 * Remark : 인덱스 페이지 관리자 접속현황 리스트 조회 Dto
 * 사용 메서드 :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminConnectListSubDto {

    private AuthorityRole  roleCode; // 권한코드

    private Long adminId;

    private AuthorityRole roleName; // 권한명

    private String knName;

    private LocalDateTime knLastLoginDate;

    public String getRoleName() {
        return roleName.getDesc();
    }

    public String getRoleCode() {
        return roleCode.getCode();
    }

}
