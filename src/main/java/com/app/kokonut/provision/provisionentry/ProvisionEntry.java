package com.app.kokonut.provision.provisionentry;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Woody
 * LocalDateTime : 2023-05-08
 * Time :
 * Remark : 개인정보제공 제공항목 테이블
 */
@Entity
@EqualsAndHashCode(of="pipeId")
@Data
@NoArgsConstructor
@Table(name="kn_personal_info_provision_entry")
public class ProvisionEntry {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "pipe_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pipeId;

    @ApiModelProperty("개인정보제공 고유코드")
    @Column(name = "pi_code")
    private String piNumber;

    @ApiModelProperty("제공할 테이블명")
    @Column(name = "pipe_table_name")
    private String pipe_table_name;

    @ApiModelProperty("제공할 필드코드(,) 구분자")
    @Column(name = "pipe_table_target")
    private String pipe_table_target;

    @ApiModelProperty("등록자 email")
    @Column(name = "insert_email")
    private String insert_email;

    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date")
    private LocalDateTime insert_date;

}
