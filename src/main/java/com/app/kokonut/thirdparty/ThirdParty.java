package com.app.kokonut.thirdparty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-07-24
 * Time :
 * Remark : 서드파티 Table Entity
 */
@Entity
@EqualsAndHashCode(of = "tsId")
@Data
@NoArgsConstructor
@Table(name="kn_thirdparty")
public class ThirdParty {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "ts_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tsId;

    @ApiModelProperty("회사코드")
    @Column(name = "cp_code")
    private String cpCode;

    @ApiModelProperty("서트파티 설정타입 - '1' : 비즈엠")
    @Column(name = "ts_type")
    private String tsType;

    @ApiModelProperty("등록자 email")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

    @ApiModelProperty("수정자 email")
    @Column(name = "modify_email")
    private String modify_email;

    @ApiModelProperty("수정 날짜")
    @Column(name = "modify_date")
    private LocalDateTime modify_date;
}
