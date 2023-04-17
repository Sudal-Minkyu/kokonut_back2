package com.app.kokonut.bizMessage.friendtalkMessage.friendtalkMessageRecipient;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "fmrId")
@Data
@NoArgsConstructor
@Table(name="kn_friendtalk_message_recipient")
public class FriendtalkMessageRecipient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "fmr_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fmrId;

    @Column(name = "fm_id")
    private Long fmId;

    @Column(name = "fmr_email")
    private String fmrEmail;

    @Column(name = "fmr_name")
    private String fmrName;

    @Column(name = "fmr_phone_number")
    private String fmrPhoneNumber;

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
