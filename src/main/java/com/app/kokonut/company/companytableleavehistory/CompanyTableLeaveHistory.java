package com.app.kokonut.company.companytableleavehistory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "ctlhId")
@Data
@NoArgsConstructor
@Table(name="kn_company_table_leave_history")
public class CompanyTableLeaveHistory {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "ctlh_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ctlhId;

    @ApiModelProperty("회사코드")
    @Column(name = "cp_code")
    private String cpCode;

    @ApiModelProperty("테이블명(= 회사코드+_+테이블 순번)")
    @Column(name = "ct_name")
    private String ctName;

    @ApiModelProperty("탈퇴한 kokonut_IDX")
    @Column(name = "ctlh_idx")
    private String ctlhIdx;

    @ApiModelProperty("탈퇴한 날짜")
    @Column(name = "ctlh_leave_date")
    private LocalDateTime ctlhLeaveDate;

}
