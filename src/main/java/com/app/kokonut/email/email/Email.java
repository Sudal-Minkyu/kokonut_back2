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

    @ApiModelProperty("이메일 그룹 주키")
    @Column(name = "eg_id")
    private Long egId;

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

    @ApiModelProperty("메일상태(1:미발송, 2:발송대기중, 3:발송중, 4:발송완료)")
    @Column(name = "em_state")
    private String emState;

    @ApiModelProperty("발송자 email")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("발송 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}
