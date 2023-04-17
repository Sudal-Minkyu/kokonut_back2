package com.app.kokonut.notice;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "ntId")
@Data
@NoArgsConstructor
@Table(name="kn_notice")
public class Notice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "nt_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ntId;

    /**
     * 최종 등록한 사람의 IDX를 저장
     */
    @Column(name = "admin_id")
    @ApiModelProperty("최종 등록한 사람의 IDX를 저장")
    private Long adminId;

    /**
     * 상단공지여부(0:일반,1:상단공지)
     */
    @Column(name = "nt_is_notice")
    @ApiModelProperty("상단공지여부(0:일반,1:상단공지)")
    private Integer ntIsNotice;

    /**
     * 제목
     */
    @Column(name = "nt_title")
    @ApiModelProperty("제목")
    private String ntTitle;

    /**
     * 내용
     */
    @ApiModelProperty("내용")
    @Column(name = "nt_content")
    private String ntContent;

    /**
     * 조회수(사용여부확인필요)
     */
    @Column(name = "nt_view_count")
    @ApiModelProperty("조회수(사용여부확인필요)")
    private Integer ntViewCount;

    /**
     * 작성정보 작성자
     */
    @ApiModelProperty("작성정보 작성자")
    @Column(name = "nt_register_name")
    private String ntRegisterName;

    /**
     * 게시일자
     */
    @ApiModelProperty("게시일자")
    @Column(name = "nt_regist_date", nullable = false)
    private LocalDateTime ntRegistDate;

    /**
     * 0:게시중지,1:게시중,2:게시대기
     */
    @Column(name = "nt_state")
    @ApiModelProperty("0:게시중지,1:게시중,2:게시대기")
    private Integer ntState;

    /**
     * 게시중지 일자
     */
    @Column(name = "nt_stop_date")
    @ApiModelProperty("게시중지 일자")
    private LocalDateTime ntStopDate;

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
