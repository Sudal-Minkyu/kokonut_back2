package com.app.kokonut.awskmshistory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@EqualsAndHashCode(of = "akhIdx")
@Data
@NoArgsConstructor
@Table(name="kn_aws_kms_history")
public class AwsKmsHistory {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "akh_idx", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long akhIdx;

    @Column(name = "cp_code")
    private String cpCode;

    @Column(name = "akh_count")
    @ApiModelProperty("KMS 호출 카운팅")
    private Long akhCount;

    @ApiModelProperty("년월(yyyymm)")
    @Column(name = "akh_yyyymm")
    private String akhYyyymm;

    @ApiModelProperty("최근 호출한 날짜")
    @Column(name = "modify_date")
    private LocalDate modify_date;

}
