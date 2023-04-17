package com.app.kokonut.apiKey;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2022-10-25
 * Time :
 * Remark : api_key Table Entity
 */
@Entity
@EqualsAndHashCode(of = "akId")
@Data
@NoArgsConstructor
@Table(name="kn_api_key")
public class ApiKey implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "ak_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long akId;

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
     * API KEY
     */
    @ApiModelProperty("API KEY")
    @Column(name = "ak_key", nullable = false)
    private String akKey;

    /**
     * 사용여부
     */
    @Column(name = "ak_use_yn")
    @ApiModelProperty("사용여부")
    private String akUseYn;

    /**
     * 해제사유
     */
    @Column(name = "ak_reason")
    @ApiModelProperty("해제사유")
    private String akReason;

    /**
     * 허용 IP 1
     */
    @Column(name = "ak_agree_ip_1")
    @ApiModelProperty("허용 IP 1")
    private String akAgreeIp1;

    /**
     * 허용 IP 1의 메모내용
     */
    @Column(name = "ak_agree_memo_1")
    @ApiModelProperty("허용 IP 1의 대한 메모내용")
    private String akAgreeMemo1;

    /**
     * 허용 IP 2
     */
    @Column(name = "ak_agree_ip_2")
    @ApiModelProperty("허용 IP 2")
    private String akAgreeIp2;

    /**
     * 허용 IP 2의 메모내용
     */
    @Column(name = "ak_agree_memo_2")
    @ApiModelProperty("허용 IP 2의 대한 메모내용")
    private String akAgreeMemo2;

    /**
     * 허용 IP 3
     */
    @Column(name = "ak_agree_ip_3")
    @ApiModelProperty("허용 IP 3")
    private String akAgreeIp3;

    /**
     * 허용 IP 3의 메모내용
     */
    @Column(name = "ak_agree_memo_3")
    @ApiModelProperty("허용 IP 3의 대한 메모내용")
    private String akAgreeMemo3;

    /**
     * 허용 IP 4
     */
    @Column(name = "ak_agree_ip_4")
    @ApiModelProperty("허용 IP 4")
    private String akAgreeIp4;

    /**
     * 허용 IP 4의 메모내용
     */
    @Column(name = "ak_agree_memo_4")
    @ApiModelProperty("허용 IP 4의 대한 메모내용")
    private String akAgreeMemo4;

    /**
     * 허용 IP 5
     */
    @Column(name = "ak_agree_ip_5")
    @ApiModelProperty("허용 IP 5")
    private String akAgreeIp5;

    /**
     * 허용 IP 5의 메모내용
     */
    @Column(name = "ak_agree_memo_5")
    @ApiModelProperty("허용 IP 5의 대한 메모내용")
    private String akAgreeMemo5;

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
