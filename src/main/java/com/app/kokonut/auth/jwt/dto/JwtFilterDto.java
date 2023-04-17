package com.app.kokonut.auth.jwt.dto;

import com.app.kokonut.admin.enums.AuthorityRole;
import lombok.Builder;
import lombok.Data;

/**
 * @author Woody
 * Date : 2022-12-30
 * Time :
 * Remark : JWT를 통해 해당 유저의 정보를 받아오는 Dto
 */
@Data
public class JwtFilterDto {

    private String email;

    private AuthorityRole role;

    @Builder
    public JwtFilterDto(String email, AuthorityRole role) {
        this.email = email;
        this.role = role;
    }

}