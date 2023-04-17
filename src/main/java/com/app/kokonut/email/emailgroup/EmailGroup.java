package com.app.kokonut.email.emailgroup;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "egId")
@Data
@NoArgsConstructor
@Table(name="kn_email_group")
public class EmailGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "eg_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long egId;

    /**
     * 관리자 키(문자열, 구분자: ',')
     */
    @ApiModelProperty("관리자 키(문자열, 구분자: ',')")
    @Column(name = "eg_admin_id_list", nullable = false)
    private String egAdminIdList;

    /**
     * 그룹명
     */
    @ApiModelProperty("그룹명")
    @Column(name = "eg_name", nullable = false)
    private String egName;

    /**
     * 그룹설명
     */
    @ApiModelProperty("그룹설명")
    @Column(name = "eg_desc", nullable = false)
    private String egDesc;

    /**
     * 사용여부
     */
    @ApiModelProperty("사용여부")
    @Column(name = "eg_use_yn", nullable = false)
    private String egUseYn;

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

    /**
     * 관리자 이메일 목록 (문자열, 구분자: ',')
     * 영속성 제외, db 컬럼 아님.
     * 엔티티 매핑 무시
     */
    @Transient
    private String adminEmailList;

}
