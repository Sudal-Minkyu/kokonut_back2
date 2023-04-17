package com.app.kokonut.qna.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QnaSchedulerDto implements Serializable {

    @ApiModelProperty("키")
    private Long qnaId;

    @ApiModelProperty("제목")
    private String qnaTitle;

    @ApiModelProperty("질문등록일시")
    private LocalDateTime insert_date;

}
