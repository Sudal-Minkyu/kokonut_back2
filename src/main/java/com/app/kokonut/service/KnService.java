package com.app.kokonut.service;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "srId")
@Data
@NoArgsConstructor
@Table(name="kn_service")
public class KnService implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "sr_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long srId;

    /**
     * 서비스 이름
     */
    @ApiModelProperty("서비스 이름")
    @Column(name = "sr_service")
    private String srService;

    /**
     * 서비스 금액
     */
    @ApiModelProperty("서비스 금액")
    @Column(name = "sr_price")
    private Integer srPrice;

    /**
     * 평균 회원 1명당 금액
     */
    @ApiModelProperty("평균 회원 1명당 금액")
    @Column(name = "sr_per_price")
    private Integer srPerPrice;

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
