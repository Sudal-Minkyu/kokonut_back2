package com.app.kokonutdormant.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Woody
 * Date : 2023-01-12
 * Time :
 * Remark : 휴면테이블의 필드와 테이블명을 통해 존재여부를 체크하는 Dto
 */
@Setter
@Getter
public class KokonutDormantFieldCheckDto {

    private final String TABLE_NAME;

    private final String COLUMN_NAME;

    public KokonutDormantFieldCheckDto(String TABLE_NAME, String COLUMN_NAME) {
        this.TABLE_NAME = TABLE_NAME;
        this.COLUMN_NAME = COLUMN_NAME;
    }

}
