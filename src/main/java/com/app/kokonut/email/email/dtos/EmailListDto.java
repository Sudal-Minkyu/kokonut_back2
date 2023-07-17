package com.app.kokonut.email.email.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailListDto {

    private String emPurpose; // 발송목적(1:주요공지, 2:광고/홍보, 3:기타)

    private String emEtc; // 기타일 경우 해당 내용

    private String emRequestId; // 이메일전송 고유 requestId 값 -> 예약취소가 될 경우

//    private String emTitle; // 제목

    private String emState; // 메일상태(1: 발송중, 2: 발송예약중, 3: 일부실패, 4: 발송실패, 5: 발송완료, 6: 발송취소)

    private Integer emSendAllCount; // 이메일발송 전체건수

    private Integer emSendSucCount; // 이메일발송 성공건수

    private Integer emSendFailCount; // 이메일발송 실패건수

    private String knName; // 보낸사람 이름

    private String insert_email; // 보낸사람 이메일

    private String emEmailSend; // 발신자 이메일

    private LocalDateTime send_date; // 발송날짜 예약 발송이 일경우 emReservationDate값을 보냄 그게아니면 insert_date : (emType로 비교)



}
