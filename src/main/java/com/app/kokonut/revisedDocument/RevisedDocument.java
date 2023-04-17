package com.app.kokonut.revisedDocument;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Joy
 * Date : 2023-01-14
 * Time :
 * Remark : 개인정보 처리방침 개정문서 테이블
 */
@Entity
@EqualsAndHashCode(of = "rdId")
@Data
@NoArgsConstructor
@Table(name="kn_revised_document")
public class RevisedDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty("키")
    @Column(name = "rd_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rdId;

    /**
     * 등록자
     */
    @ApiModelProperty("등록자")
    @Column(name = "admin_id")
    private Long adminId;

    @ApiModelProperty("시행시작일자")
    @Column(name = "rd_enforce_start_date")
    private LocalDateTime rdEnforceStartDate;

    @ApiModelProperty("시행종료일자")
    @Column(name = "rd_enforce_end_date")
    private LocalDateTime rdEnforceEndDate;

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
