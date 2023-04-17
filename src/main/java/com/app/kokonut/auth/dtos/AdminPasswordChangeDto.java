package com.app.kokonut.auth.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author Woody
 * Date : 2023-03-13
 * Time :
 * Remark : 사업자 비밀번호찾기시 받는 -> 변경 DTO
 */
@Data
public class AdminPasswordChangeDto {

    private String knEmail;

    private String tempPwd;

    private String knPassword;

    private String knPasswordConfirm;

}
