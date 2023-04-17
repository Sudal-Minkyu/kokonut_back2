package com.app.kokonut.refactor.privacyEmailHistory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@EqualsAndHashCode(of = "phId")
@Data
@NoArgsConstructor
@Table(name="kn_privacy_email_history")
public class PrivacyEmailHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "ph_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long phId;

    /**
     * privacy_email 키
     */
    @ApiModelProperty("privacy_email 키")
    @Column(name = "pe_id", nullable = false)
    private Long peId;

    /**
     * 받는 사람 이메일
     */
    @ApiModelProperty("받는 사람 이메일")
    @Column(name = "ph_receiver_email", nullable = false)
    private String phReceiverEmail;

    /**
     * 발송일
     */
    @ApiModelProperty("발송일")
    @Column(name = "ph_send_date", nullable = false)
    private Date phSendDate;

}
