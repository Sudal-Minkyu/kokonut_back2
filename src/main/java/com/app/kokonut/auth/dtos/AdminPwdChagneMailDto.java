package com.app.kokonut.auth.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Woody
 * Date : 2023-08-09
 * Time :
 * Remark : 메일로 전송받은 비밀번호변경 Dto
 */
@Data
public class AdminPwdChagneMailDto {

    private String userEmail;

    @NotBlank(message = "비밀번호는 필수 입력값 입니다.")
    private String knPassword;

    private String knPasswordConfirm; // 비밀번호 체크

}
