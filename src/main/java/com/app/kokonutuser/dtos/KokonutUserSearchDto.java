package com.app.kokonutuser.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-01-06
 * Time :
 * Remark : 유저 리스트 조회 Search Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KokonutUserSearchDto {

    private String status; // 상태 : "1" 정상, "2" 휴면

    private String baseDate; // 목록보기 : "" 전체, "REGISTER_DATE" 회원가입일순, "LAST_LOGIN_DATE" 최근접속일순

    private String searchText; // 아이디

    private LocalDateTime stimeStart; // 시작 날짜

    private LocalDateTime stimeEnd; // 끝 날짜

}