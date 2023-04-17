package com.app.kokonut.faq.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * @author Joy
 * Date : 2023-01-03
 * Time :
 * Remark : 자주묻는 질문 목록+내용 조회 시 사용하는 DTO (제목, 내용만 보여주는 간단한 목록 형태)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaqAnswerListDto implements Serializable {

    @ApiModelProperty("질문")
    private String faqQuestion;

    @ApiModelProperty("답변")
    private String faqAnswer;

}
