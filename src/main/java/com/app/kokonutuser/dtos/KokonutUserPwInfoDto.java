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

    private final Long IDX;

    private final String PASSWORD;

    public KokonutUserPwInfoDto(long IDX, String PASSWORD) {
        this.IDX = IDX;
        this.PASSWORD = PASSWORD;
    }

}
