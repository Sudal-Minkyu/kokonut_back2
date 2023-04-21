package com.app.kokonut.awsKmsHistory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

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

    /**
     * 호출 타입
     */
    @Column(name = "akh_type")
    @ApiModelProperty("호출 타입")
    private String akhType;

    /**
     * 호출 날짜
     */
    @ApiModelProperty("호출 날짜")
    @Column(name = "akh_regdate", nullable = false)
    private LocalDateTime akhRegdate;

}
