package com.app.kokonutuser.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Woody
 * Date : 2023-01-04
 * Time :
 * Remark : 조회한 기업의 유저테이블의 IDX와 Field를 통해 해당 필드의 값을 조회하는 Dto
 */
@Setter
@Getter
public class KokonutUserFieldInfoDto {

    private final Long IDX;

    private final Object VALUE;

    public KokonutUserFieldInfoDto(long IDX, Object VALUE) {
        this.IDX = IDX;
        this.VALUE = VALUE;
    }

}
