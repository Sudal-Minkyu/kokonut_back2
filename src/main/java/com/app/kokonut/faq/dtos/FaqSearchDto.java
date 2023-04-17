package com.app.kokonut.faq.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaqSearchDto implements Serializable {

    private Integer faqState;  // 상태(0:게시중지,1:게시중,2:게시대기)

    private Integer faqType;   // 분류(0:기타,1:회원정보,2:사업자정보,3:kokonut서비스,4:결제)

    private LocalDateTime stimeStart; // 시작 날짜

    private LocalDateTime stimeEnd; // 끝 날짜

    private String searchText;  // 검색어

}
