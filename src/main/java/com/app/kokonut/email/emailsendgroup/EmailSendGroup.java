package com.app.kokonut.email.emailsendgroup;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "egId")
@Data
@NoArgsConstructor
@Table(name="kn_email_send_group")
public class EmailSendGroup {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "eg_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long egId;

    @ApiModelProperty("관리자 키(문자열, 구분자: ',')")
    @Lob
    @Column(columnDefinition = "LONGTEXT", name="eg_kokonut_IDX_list")
    private String egKokonutIDXList;

    @ApiModelProperty("등록자 email")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}
