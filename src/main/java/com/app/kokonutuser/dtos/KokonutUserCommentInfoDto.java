package com.app.kokonutuser.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Woody
 * Date : 2023-10-06
 * Time :
 * Remark :
 */
@Setter
@Getter
public class KokonutUserCommentInfoDto {

    private final String columnName;

    private final String columnSecurity;

    private final String columnSubName;

    public KokonutUserCommentInfoDto(String columnName, String columnSecurity, String columnSubName) {
        this.columnName = columnName;
        this.columnSecurity = columnSecurity;
        this.columnSubName = columnSubName;
    }

}
