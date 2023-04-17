package com.app.kokonut.faq.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaqListDto implements Serializable {

    @ApiModelProperty("키")
    private Long faqId;

    @ApiModelProperty("질문")
    private String faqQuestion;

    @ApiModelProperty("답변")
    private String faqAnswer;

    @ApiModelProperty("분류(0:기타,1:회원정보,2:사업자정보,3:kokonut서비스,4:결제)")
    private Integer faqType;

    @ApiModelProperty("조회수")
    private Integer faqViewCount;

    @ApiModelProperty("등록일자")
    private LocalDateTime insert_date;

}
