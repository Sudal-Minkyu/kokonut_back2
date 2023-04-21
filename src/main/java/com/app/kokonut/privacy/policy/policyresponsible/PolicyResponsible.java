package com.app.kokonut.privacy.policy.policyresponsible;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of="pirId")
@Data
@NoArgsConstructor
@Table(name="kn_privacy_policy_info_responsible")
public class PolicyResponsible {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "pir_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pirId;

    @ApiModelProperty("개인정보처리방침 ID")
    @Column(name = "pi_id")
    private Long piId;

    @ApiModelProperty("성명")
    @Column(name = "pir_name")
    private String pirName;

    @ApiModelProperty("직책")
    @Column(name = "pir_position")
    private String pirPosition;

    @ApiModelProperty("이메일")
    @Column(name = "pir_email")
    private String pirEmail;

    @ApiModelProperty("연락처")
    @Column(name = "pir_contact")
    private String pirContact;

    @ApiModelProperty("담당부서")
    @Column(name = "pir_department")
    private String pirDepartment;

    @ApiModelProperty("등록한 이메일")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록된 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}