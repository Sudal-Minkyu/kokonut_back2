package com.app.kokonut.awsKmsHistory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@EqualsAndHashCode(of = "akhIdx")
@Data
@NoArgsConstructor
@Table(name="kn_aws_kms_history")
public class AwsKmsHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
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
