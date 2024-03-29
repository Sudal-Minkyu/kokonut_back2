package com.app.kokonut.provision.provisionroster;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Woody
 * LocalDateTime : 2023-05-08
 * Time :
 * Remark : 개인정보제공 명단 테이블
 */
@Entity
@EqualsAndHashCode(of="piprId")
@Data
@NoArgsConstructor
@Table(name="kn_personal_info_provision_roster")
public class ProvisionRoster {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "pipr_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long piprId;

    @ApiModelProperty("개인정보제공 고유코드")
    @Column(name = "pro_code")
    private String proCode;

    @ApiModelProperty("제공된 관리자id")
    @Column(name = "admin_id")
    private Long adminId;

    @ApiModelProperty("등록자 email")
    @Column(name = "insert_email")
    private String insert_email;

    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date")
    private LocalDate insert_date;

}
