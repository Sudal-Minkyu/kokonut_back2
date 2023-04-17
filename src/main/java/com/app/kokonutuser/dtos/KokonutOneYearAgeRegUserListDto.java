package com.app.kokonutuser.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Woody
 * Date : 2023-01-03
 * Time :
 * Remark : 코코넛 USER 1년전에 가입한 회원목록 조회 ListDto
 */
@Setter
@Getter
public class KokonutOneYearAgeRegUserListDto {

    // 받아오는 값이 바뀌거나 추가 될 수 있음. 2023/01/03 - woody
    private Integer IDX;
    private String NAME;

}
