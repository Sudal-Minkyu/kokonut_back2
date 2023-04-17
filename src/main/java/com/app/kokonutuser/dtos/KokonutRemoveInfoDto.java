package com.app.kokonutuser.dtos;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-01-03
 * Time :
 * Remark : 코코넛 Remove DB로 보낼 회원 조회 -> User,Dormant 공통사용 Dto
 */
@Setter
@Getter
public class KokonutRemoveInfoDto {

    private Long IDX;
    private String ID;
    private LocalDateTime REGDATE;

}
