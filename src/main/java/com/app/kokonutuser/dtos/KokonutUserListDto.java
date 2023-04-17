package com.app.kokonutuser.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Woody
 * Date : 2023-01-06
 * Time :
 * Remark : 유저 리스트 조회 ListDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KokonutUserListDto {

    private Long IDX; // IDX

    private String ID; // 아이디

    private Timestamp REGDATE; // 회원가입 일시

    private Timestamp LAST_LOGIN_DATE; // 최근 접속 일시

    public KokonutUserListDto(long IDX, String ID, Timestamp REGDATE, Timestamp LAST_LOGIN_DATE) {
        this.IDX = IDX;
        this.ID = ID;
        this.REGDATE = REGDATE;
        this.LAST_LOGIN_DATE = LAST_LOGIN_DATE;
    }

}