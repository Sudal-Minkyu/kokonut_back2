package com.app.kokonut.provision.provisiondownloadhistory;

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
 * Remark : 개인정보제공 다운로드 이력 테이블
 */
@Entity
@EqualsAndHashCode(of="piphId")
@Data
@NoArgsConstructor
@Table(name="kn_personal_info_provision_histroy")
public class ProvisionDownloadHistory {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "piph_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long piphId;

    @ApiModelProperty("개인정보제공 고유코드")
    @Column(name = "pro_code")
    private String proCode;

    @ApiModelProperty("다운로드한 관리자id")
    @Column(name = "admin_id")
    private Long adminId;

    @ApiModelProperty("다운로드 횟수")
    @Column(name = "piph_count")
    private Integer piphCount;

    @ApiModelProperty("등록자 email")
    @Column(name = "insert_email")
    private String insert_email;

    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date")
    private LocalDateTime insert_date;

}
