package com.app.kokonut.bizMessage.alimtalkMessage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "amId")
@Data
@NoArgsConstructor
@Table(name="kn_alimtalk_message")
public class AlimtalkMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "am_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long amId;

    /**
     * 관리자 키
     */
    @ApiModelProperty("관리자 키")
    @Column(name = "admin_id")
    private Long adminId;

    /**
     * 채널ID
     */
    @ApiModelProperty("채널ID")
    @Column(name = "kc_channel_id", nullable = false)
    private String kcChannelId;

    /**
     * 템플릿 코드
     */
    @ApiModelProperty("템플릿 코드")
    @Column(name = "at_template_code")
    private String atTemplateCode;

    /**
     * 요청ID(예약발송시 reserveId로 사용)
     */
    @Column(name = "am_request_id", nullable = false)
    @ApiModelProperty("요청ID(예약발송시 reserveId로 사용)")
    private String amRequestId;

    /**
     * 발송타입-즉시발송(immediate),예약발송(reservation)
     */
    @Column(name = "am_transmit_type")
    @ApiModelProperty("발송타입-즉시발송(immediate),예약발송(reservation)")
    private String amTransmitType;

    /**
     * 예약발송일시
     */
    @ApiModelProperty("예약발송일시")
    @Column(name = "am_reservation_date")
    private LocalDateTime amReservationDate;

    /**
     * 발송상태(init-초기상태,[메시지발송요청조회]success-성공,processing-발송중,reserved-예약중,scheduled-스케줄중,fail-실패 [예약메시지]ready-발송 대기,processing-발송 요청중,canceled-발송 취소,fail-발송 요청 실패,done-발송 요청 성공,stale-발송 요청 실패 (시간 초과))
     */
    @Column(name = "am_status")
    @ApiModelProperty("발송상태(init-초기상태,[메시지발송요청조회]success-성공,processing-발송중,reserved-예약중,scheduled-스케줄중,fail-실패 [예약메시지]ready-발송 대기,processing-발송 요청중,canceled-발송 취소,fail-발송 요청 실패,done-발송 요청 성공,stale-발송 요청 실패 (시간 초과))")
    private String amStatus;

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
