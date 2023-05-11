package com.app.kokonut.provision.provisionlist;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Woody
 * LocalDateTime : 2023-05-08
 * Time :
 * Remark : 개인정보제공 제공할 개인정보 명단 테이블
 */
@Entity
@EqualsAndHashCode(of="piplId")
@Data
@NoArgsConstructor
@Table(name="kn_personal_info_provision_list")
public class ProvisionList {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "pipl_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long piplId;

    @ApiModelProperty("개인정보제공 고유코드")
    @Column(name = "pro_code")
    private String proCode;

    @ApiModelProperty("'제공할 kokonut_IDX 리스트(,) 구분자'")
    @Column(name = "pipl_target_idxs")
    private String piplTargetIdxs;

    @ApiModelProperty("등록자 email")
    @Column(name = "insert_email")
    private String insert_email;

    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date")
    private LocalDateTime insert_date;

}
