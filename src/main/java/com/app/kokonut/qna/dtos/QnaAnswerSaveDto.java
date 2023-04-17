package com.app.kokonut.qna.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Joy
 * Date : 2022-12-28
 * Time :
 * Remark : 1:1 문의글 답변 등록 Dto
 */
@Getter
@Setter
@NoArgsConstructor
public class QnaAnswerSaveDto {

    @ApiModelProperty("키")
    private Long qnaId;

    @ApiModelProperty("답변 내용")
    private String qnaAnswer;

}
