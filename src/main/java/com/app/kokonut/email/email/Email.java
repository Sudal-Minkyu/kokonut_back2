package com.app.kokonut.email.email;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "emId")
@Data
@NoArgsConstructor
@Table(name="kn_email")
public class Email {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "em_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emId;

    @ApiModelProperty("회사코드")
    @Column(name = "cp_code")
    private String cpCode;

    @ApiModelProperty("발송타입(1 : 일반발송, 2 : 예약발송)")
    @Column(name = "em_type")
    private String emType;

    @ApiModelProperty("예약발송일 경우 발송시간")
    @Column(name = "em_reservation_date")
    private LocalDateTime emReservationDate;

    @ApiModelProperty("발송대상(1 : 전체회원, 2 : 선택회원)")
    @Column(name = "em_receiver_type")
    private String emReceiverType;

    @ApiModelProperty("발송목적(1:주요공지, 2:광고/홍보, 3:기타)")
    @Column(name = "em_purpose")
    private String emPurpose;

    @ApiModelProperty("기타일 경우 해당 내용")
    @Column(name = "em_etc")
    private String emEtc;

    @ApiModelProperty("발신자 이메일")
    @Column(name = "em_email_send")
    private String emEmailSend;

    @ApiModelProperty("제목")
    @Column(name = "em_title")
    private String emTitle;

    @ApiModelProperty("내용")
    @Lob
    @Column(columnDefinition = "LONGTEXT", name="em_contents")
    private String emContents;

    @ApiModelProperty("메일상태(1: 발송중, 2: 발송예약중, 3: 일부실패, 4: 발송실패, 5: 발송완료, 6: 발송취소)")
    @Column(name = "em_state")
    private String emState;

    @ApiModelProperty("이메일전송 고유 requestId 값")
    @Column(name = "em_request_id")
    private String emRequestId;

    @ApiModelProperty("이메일발송 전체건수")
    @Column(name = "em_send_all_count")
    private Integer emSendAllCount;

    @ApiModelProperty("이메일발송 성공건수")
    @Column(name = "em_send_suc_count")
    private Integer emSendSucCount;

    @ApiModelProperty("이메일발송 실패건수")
    @Column(name = "em_send_fail_count")
    private Integer emSendFailCount;

    @ApiModelProperty("발송자 email")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("발송 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

    @ApiModelProperty("수정자 email")
    @Column(name = "modify_email")
    private String modify_email;

    @ApiModelProperty("수정 날짜")
    @Column(name = "modify_date")
    private LocalDateTime modify_date;

}
