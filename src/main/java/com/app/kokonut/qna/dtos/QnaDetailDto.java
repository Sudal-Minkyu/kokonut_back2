package com.app.kokonut.qna.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author Woody
 * Date : 2023-04-01
 * Time :
 * Remark : Qna 상세보기 조회 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QnaDetailDto {

    @ApiModelProperty("키")
    private Long qnaId;

    @ApiModelProperty("제목")
    private String qnaTitle;

    @ApiModelProperty("문의내용")
    private String qnaContent;

    @ApiModelProperty("분류(0:기타,1:회원정보,2:사업자정보,3:Kokonut서비스,4:결제)")
    private Integer qnaType;

    @ApiModelProperty("상태(0:답변대기,1:답변완료)")
    private Integer qnaState;

    @ApiModelProperty("작성자 이메일")
    private String insert_email;

    @ApiModelProperty("답변 내용")
    private String qnaAnswer;

    @ApiModelProperty("답변일")
    private LocalDateTime modify_date;

    public String getQnaAnswer() {
        return Objects.requireNonNullElse(qnaAnswer, "");
    }

    public String getModify_date() {
        if(modify_date != null) {
            return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(modify_date);
        } else {
            return "";
        }
    }
}
