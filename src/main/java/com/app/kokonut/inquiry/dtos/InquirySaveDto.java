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

    private Integer iqState; // 1 : 협업문의 ,2 : 도입문의

    private String iqTitle; // 제목

    private String iqGroup; // 단체명

    private Integer iqField; // 분야 - 1 : 스타트업, 2 : 중소기업, 3 : 중견기업/대기업, 4 : 소상공인, 5 : 단체/협회, 6 : 기타

    private String iqWriter; // 작성자

    private String iqEmail; // 이메일

    private String iqContents; // 내용

}
