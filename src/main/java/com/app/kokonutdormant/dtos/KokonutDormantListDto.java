package com.app.kokonutdormant.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author Woody
 * Date : 2023-01-06
 * Time :
 * Remark : 휴면 리스트 조회 ListDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KokonutDormantListDto {

    private Long IDX; // IDX

    private String ID; // 아이디

    private Timestamp REGDATE; // 회원가입 일시

    private Timestamp LAST_LOGIN_DATE; // 최근 접속 일시

    public KokonutDormantListDto(long IDX, String ID, Timestamp REGDATE, Timestamp LAST_LOGIN_DATE) {
        this.IDX = IDX;
        this.ID = ID;
        this.REGDATE = REGDATE;
        this.LAST_LOGIN_DATE = LAST_LOGIN_DATE;
    }

}