package com.app.kokonut.privacy.policy.policypurpose;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of="pipId")
@Data
@NoArgsConstructor
@Table(name="kn_privacy_policy_info_purpose")
public class PolicyPurpose {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "pip_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pipId;

    @ApiModelProperty("개인정보처리방침 ID")
    @Column(name = "pi_id")
    private Long piId;

    @ApiModelProperty("처리목적 제목")
    @Column(name = "pip_title")
    private String pipTitle;

    @ApiModelProperty("처리목적 내용")
    @Column(name = "pip_content")
    private String pipContent;

    @ApiModelProperty("등록한 이메일")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록된 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}