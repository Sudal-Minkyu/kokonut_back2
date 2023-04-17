package com.app.kokonut.bizMessage.friendtalkMessage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@EqualsAndHashCode(of = "fmId")
@Data
@NoArgsConstructor
@Table(name="kn_friendtalk_message")
public class FriendtalkMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "fm_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fmId;

    /**
     * 회사(Company) 키
     */
    @Column(name = "company_id")
    @ApiModelProperty("회사(Company) 키")
    private Long companyId;

    /**
     * 등록자
     */
    @ApiModelProperty("등록자")
    @Column(name = "admin_id")
    private Long adminId;

    /**
     * 채널ID
     */
    @ApiModelProperty("채널ID")
    @Column(name = "kc_channel_id", nullable = false)
    private String kcChannelId;

    /**
     * 요청ID(예약발송시 reserveId로 사용)
     */
    @Column(name = "fm_request_id", nullable = false)
    @ApiModelProperty("요청ID(예약발송시 reserveId로 사용)")
    private String fmRequestId;

    /**
     * 발송타입-즉시발송(immediate),예약발송(reservation)
     */
    @Column(name = "fm_transmit_type")
    @ApiModelProperty("발송타입-즉시발송(immediate),예약발송(reservation)")
    private String fmTransmitType;

    /**
     * 예약발송일시
     */
    @ApiModelProperty("예약발송일시")
    @Column(name = "fm_reservation_date")
    private LocalDateTime fmReservationDate;

    /**
     * 발송상태(init-초기상태,[메시지발송요청조회]success-성공,processing-발송중,reserved-예약중,scheduled-스케줄중,fail-실패 [예약메시지]ready-발송 대기,processing-발송 요청중,canceled-발송 취소,fail-발송 요청 실패,done-발송 요청 성공,stale-발송 요청 실패 (시간 초과))
     */
    @Column(name = "fm_status")
    @ApiModelProperty("발송상태(init-초기상태,[메시지발송요청조회]success-성공,processing-발송중,reserved-예약중,scheduled-스케줄중,fail-실패 [예약메시지]ready-발송 대기,processing-발송 요청중,canceled-발송 취소,fail-발송 요청 실패,done-발송 요청 성공,stale-발송 요청 실패 (시간 초과))")
    private String fmStatus;

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
