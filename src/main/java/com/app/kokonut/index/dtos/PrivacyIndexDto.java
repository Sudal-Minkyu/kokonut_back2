package com.app.kokonut.index.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Woody
 * Date : 2023-06-25
 * Time :
 * Remark : Privacy 개인정보 수 인덱스용 카운팅 조회 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivacyIndexDto {

    private LocalDate fromDate; // 시작날짜
    private LocalDate toDate; // 끝날짜

    private Integer allCount; // 전체 개인정보 수

    private Integer nowUserCount; // 기존회원 수

    private Integer newUserCount; // 신규회원 수

    private Integer leaveUserCount; // 탈퇴회원 수

    public String getFromDate() {
        return DateTimeFormatter.ofPattern("yy. MM. dd").format(fromDate);
    }

    public String getToDate() {
        return DateTimeFormatter.ofPattern("yy. MM. dd").format(toDate);
    }

}
