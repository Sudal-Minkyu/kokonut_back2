package com.app.kokonut.bizMessage.alimtalkMessage.alimtalkMessageRecipient;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "amrId")
@Data
@NoArgsConstructor
@Table(name="kn_alimtalk_message_recipient")
public class AlimtalkMessageRecipient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "amr_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long amrId;

    @Column(name = "am_id")
    private Long amId;

    @Column(name = "amr_email")
    private String amr_email;

    @Column(name = "amr_name")
    private String amr_name;

    @Column(name = "amr_phone_number")
    private String amr_phone_number;

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
