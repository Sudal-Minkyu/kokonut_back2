package com.app.kokonut.index.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-06-26
 * Time :
 * Remark : 인덱스 페이지 관리자 접속현황 리스트 조회 Dto
 * 사용 메서드 :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminConnectListDto {

    private String connectState; // 0 : "미접속중", 1 : "접속중"

    private String  roleName; // 권한명

    private String knName;

    private String connectTime; // 현재날짜일시 - 최근접속일시 = 마지막로그인 일시

}
