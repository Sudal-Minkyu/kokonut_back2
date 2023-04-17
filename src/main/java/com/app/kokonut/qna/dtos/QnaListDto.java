package com.app.kokonut.qna.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Joy
 * Date : 2022-12-28
 * Time :
 * Remark : Qna 목록 조회 ListDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QnaListDto implements Serializable {

    private Long qnaId;

    @ApiModelProperty("제목")
    private String qnaTitle;

    @ApiModelProperty("분류(0:기타, 1:회원정보, 2:사업자정보, 3:Kokonut서비스, 4:결제)")
    private Integer qnaType;

    @ApiModelProperty("문의등록일시")
    private LocalDateTime insert_date;

    @ApiModelProperty("상태(0:답변대기,1:답변완료)")
    private Integer qnaState;

    public String getInsert_date() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(insert_date);
    }

}
