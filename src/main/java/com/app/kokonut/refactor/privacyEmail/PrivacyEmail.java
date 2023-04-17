package com.app.kokonut.refactor.privacyEmail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@EqualsAndHashCode(of = "peId")
@Data
@NoArgsConstructor
@Table(name="kn_privacy_email")
public class PrivacyEmail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "pe_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer peId;

    /**
     * 기업 키(기업별 이메일인 경우)
     */
    @Column(name = "company_id")
    @ApiModelProperty("기업 키(기업별 이메일인 경우)")
    private Long companyId;

    /**
     * 발신자 이메일
     */
    @ApiModelProperty("발신자 이메일")
    @Column(name = "pe_sender_email", nullable = false)
    private String peSenderEmail;

    /**
     * 제목
     */
    @ApiModelProperty("제목")
    @Column(name = "pe_title", nullable = false)
    private String peTitle;

    /**
     * 내용
     */
    @ApiModelProperty("내용")
    @Column(name = "pe_contents", nullable = false)
    private String peContents;

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
