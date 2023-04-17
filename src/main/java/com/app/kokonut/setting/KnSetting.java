package com.app.kokonut.setting;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Joy
 * LocalDateTime : 2023-01-05
 * Time :
 * Remark : 운영 셋팅관련 Table Entity
 */
@Entity
@EqualsAndHashCode(of = "stId")
@Data
@NoArgsConstructor
@Table(name="kn_setting")
public class KnSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty("키")
    @Column(name = "st_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stId;

    @Column(name = "company_id")
    @ApiModelProperty("회사(Company) 키")
    private Long companyId;

    @Column(name = "st_overseas_block")
    @ApiModelProperty("해외로그인차단(0:차단안함,1:차단)")
    private Integer stOverseasBlock;

    @Column(name = "st_dormant_account")
    @ApiModelProperty("휴면회원 전환 시(0:다른DB로 정보이관, 1:이관 없이 회원정보 삭제)")
    private Integer stDormantAccount;

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
