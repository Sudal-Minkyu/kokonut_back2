package com.app.kokonut.inquiry.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-01-19
 * Time :
 * Remark : Inquiry Save Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquirySaveDto {

    private String iqWriter; // 작성자

    private Integer iqState; // 선호 온보딩 방식  -> 1. 오프라인 미팅 2. 온라인 교육 3. 상관없음

    private String iqCompany; // 회사명

    private Integer iqService; // 서비스명

    private String iqPhone; // 연락처(휴대전화)

    private String iqEmail; // 이메일

    private String iqContents; // "온보딩 진행 시 요청사항 (이전에 내용칸 활용)"


}
