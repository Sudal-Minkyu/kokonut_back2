package com.app.kokonutapi.auth.dtos;

import lombok.Data;

/**
 * @author Woody
 * Date : 2023-04-27
 * Time :
 * Remark :
 */
@Data
public class AuthApiLoginDto {

    private String kokonutId; // 받은 아이디

    private String kokonutPw; // 받은 비밀번호

}
