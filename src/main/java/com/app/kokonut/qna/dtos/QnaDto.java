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
public class QnaDto implements Serializable {
    /**
     * 키
     */
    @ApiModelProperty("키")
    private Integer idx;


    /**
     * COMPANY IDX
     */
    @ApiModelProperty("COMPANY IDX")
    private Long companyId;


    /**
     * 질문자(사용자 키)
     */
    @ApiModelProperty("질문자(사용자 키)")
    private Long adminId;


    /**
     * 제목
     */
    @ApiModelProperty("제목")
    private String title;


    /**
     * 문의내용
     */
    @ApiModelProperty("문의내용")
    private String content;


    /**
     * 첨부파일 아이디
     */
    @ApiModelProperty("첨부파일 아이디")
    private String fileGroupId;


    /**
     * 분류(0:기타,1:회원정보,2:사업자정보,3:Kokonut서비스,4:결제)
     */
    @ApiModelProperty("분류(0:기타,1:회원정보,2:사업자정보,3:Kokonut서비스,4:결제)")
    private Integer type;


    /**
     * 질문등록일시
     */
    @ApiModelProperty("질문등록일시")
    private LocalDateTime regdate;


    /**
     * 상태(0:답변대기,1:답변완료)
     */
    @ApiModelProperty("상태(0:답변대기,1:답변완료)")
    private Integer state;


    /**
     * 질문 답변자
     */
    @ApiModelProperty("질문 답변자")
    private Integer ansIdx;


    /**
     * 답변 내용
     */
    @ApiModelProperty("답변 내용")
    private String answer;


    /**
     * 답변일
     */
    @ApiModelProperty("답변일")
    private LocalDateTime answerDate;

}
