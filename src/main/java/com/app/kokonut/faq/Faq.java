package com.app.kokonut.faq;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@EqualsAndHashCode(of = "faqId")
@Data
@NoArgsConstructor
@Table(name="kn_faq")
public class Faq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "faq_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long faqId;

    /**
     * 등록자
     */
    @ApiModelProperty("관리자 id (등록자 id)")
    @Column(name = "admin_id")
    private Long adminId;

    /**
     * 질문
     */
    @ApiModelProperty("질문")
    @Column(name = "faq_question")
    private String faqQuestion;

    /**
     * 답변
     */
    @ApiModelProperty("답변")
    @Column(name = "faq_answer")
    private String faqAnswer;

    /**
     * 분류(0:기타,1:회원정보,2:사업자정보,3:kokonut서비스,4:결제)
     */
    @Column(name = "faq_type")
    @ApiModelProperty("분류(0:기타,1:회원정보,2:사업자정보,3:kokonut서비스,4:결제)")
    private Integer faqType;

    /**
     * 게시시작일자
     */
    @ApiModelProperty("게시시작일자")
    @Column(name = "faq_regist_start_date")
    private LocalDateTime faqRegistStartDate;

    /**
     * 게시종료일자
     */
    @ApiModelProperty("게시종료일자")
    @Column(name = "faq_regist_end_date")
    private LocalDateTime faqRegistEndDate;

    /**
     * 조회수
     */
    @ApiModelProperty("조회수")
    @Column(name = "faq_view_count")
    private Integer faqViewCount;

    /**
     * 0:게시중지,1:게시중,2:게시대기
     */
    @Column(name = "faq_state")
    @ApiModelProperty("0:게시중지,1:게시중,2:게시대기")
    private Integer faqState;

    /**
     * 게시중지 일자
     */
    @Column(name = "faq_stop_date")
    @ApiModelProperty("게시중지 일자")
    private LocalDateTime faqStopDate;

    /**
     * 등록자 email
     */
    @ApiModelProperty("등록자 email")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    /**
     * 등록 날짜
     */
    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

    /**
     * 수정자
     */
    @ApiModelProperty("수정자 id")
    @Column(name = "modify_id")
    private Long modify_id;

    /**
     * 수정자 이름
     */
    @ApiModelProperty("수정자 email")
    @Column(name = "modify_email")
    private String modify_email;

    /**
     * 수정 날짜
     */
    @ApiModelProperty("수정 날짜")
    @Column(name = "modify_date")
    private LocalDateTime modify_date;

}
