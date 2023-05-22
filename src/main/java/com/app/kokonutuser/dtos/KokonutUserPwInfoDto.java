package com.app.kokonutuser.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

/**
 * @author Woody
 * Date : 2023-01-04
 * Time :
 * Remark : 유저의 비밀번호 변경시 IDX와 PASSWORD 반환해주는 Dto
 */
@Setter
@Getter
public class KokonutUserPwInfoDto {

    private final String kokonut_IDX;

    private final String PASSWORD_1_pw;

    public KokonutUserPwInfoDto(String kokonut_IDX, String PASSWORD_1_pw) {
        this.kokonut_IDX = kokonut_IDX;
        this.PASSWORD_1_pw = PASSWORD_1_pw;
    }

}
