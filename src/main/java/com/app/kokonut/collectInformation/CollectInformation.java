package com.app.kokonut.collectInformation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "ciId")
@Data
@NoArgsConstructor
@Table(name="kn_collect_information")
public class CollectInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 주키
     */
    @Id
    @ApiModelProperty("주키")
    @Column(name = "ci_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ciId;

    /**
     * 회사(Company) 키
     */
    @Column(name = "company_id")
    @ApiModelProperty("회사(Company) 키")
    private Long companyId;

    /**
     * 등록자
     */
    @ApiModelProperty("등록자")
    @Column(name = "admin_id")
    private Long adminId;

    /**
     * 제목
     */
    @Column(name = "ci_title")
    @ApiModelProperty("제목")
    private String ciTitle;

    /**
     * 내용
     */
    @ApiModelProperty("내용")
    @Column(name = "ci_content")
    private String ciContent;

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
