package com.app.kokonutdormant.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Woody
 * Date : 2023-01-12
 * Time :
 * Remark : 조회한 기업의 휴면테이블의 IDX와 Field를 통해 해당 필드의 값을 조회하는 Dto
 */
@Setter
@Getter
public class KokonutDormantFieldInfoDto {

    private final Long IDX;

    private final Object VALUE;

    public KokonutDormantFieldInfoDto(long IDX, Object VALUE) {
        this.IDX = IDX;
        this.VALUE = VALUE;
    }

}
