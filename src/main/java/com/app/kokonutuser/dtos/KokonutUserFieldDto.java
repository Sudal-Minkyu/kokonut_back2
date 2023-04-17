package com.app.kokonutuser.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Woody
 * Date : 2023-01-04
 * Time :
 * Remark : 조회한 기업의 유저테이블 필드를 조회하는 Dto
 */
@Setter
@Getter
public class KokonutUserFieldDto {

    private String Field;

    private String Type;

    private String Collation;

    private String Null;

    private String Key;

    private String Default;

    private String Extra;

    private String Privileges;

    private String Comment;

}
