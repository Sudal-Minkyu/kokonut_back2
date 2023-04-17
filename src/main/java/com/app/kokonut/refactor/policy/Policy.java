package com.app.kokonut.refactor.policy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@EqualsAndHashCode(of = "plId")
@Data
@NoArgsConstructor
@Table(name="kn_policy")
public class Policy implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "pl_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plId;

    /**
     * 정책 등록자
     */
    @Column(name = "admin_id")
    @ApiModelProperty("정책 등록자")
    private Long adminId;

    /**
     * 정책(1:서비스이용약관,2:개인정보취급방침)
     */
    @Column(name = "pl_type")
    @ApiModelProperty("정책(1:서비스이용약관,2:개인정보취급방침)")
    private Integer plType;

    /**
     * 시행일자
     */
    @ApiModelProperty("시행일자")
    @Column(name = "pl_effective_date")
    private Date plEffectiveDate;

    /**
     * HTML코드(기획상에는 파일로 되어있어 FILE_GROUP_ID로 대체될 수 있다.)
     */
    @Column(name = "pl_html")
    @ApiModelProperty("HTML코드(기획상에는 파일로 되어있어 FILE_GROUP_ID로 대체될 수 있다.)")
    private String plHtml;

    /**
     * 게시일시
     */
    @ApiModelProperty("게시일시")
    @Column(name = "pl_regist_date", nullable = false)
    private Date plRegistDate;

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
