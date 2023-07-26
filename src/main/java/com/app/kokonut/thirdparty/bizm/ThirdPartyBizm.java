package com.app.kokonut.thirdparty.bizm;

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
 * Remark : 서드파티 비즈엠설정 Table Entity
 */
@Entity
@EqualsAndHashCode(of = "tsBizmId")
@Data
@NoArgsConstructor
@Table(name="kn_thirdparty_bizm_setting")
public class ThirdPartyBizm {

    @Id
    @ApiModelProperty("서드파티 비즈엠셋팅 id")
    @Column(name = "ts_bizm_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tsBizmId;

    @ApiModelProperty("서드파티 id")
    @Column(name = "ts_id")
    private Long tsId;

    @ApiModelProperty("receiver_num 설정 고유코드")
    @Column(name = "ts_bizm_receiver_num_code")
    private String tsBizmReceiverNumCode;

    @ApiModelProperty("app_user_id 설정 고유코드")
    @Column(name = "ts_bizm_app_user_id_code")
    private String tsBizmAppUserIdCode;

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
