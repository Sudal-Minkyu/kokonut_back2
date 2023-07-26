package com.app.kokonut.email.email.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailListDto {

    // 이메일발송내역 상단 순서
    // 1. 발송목적 : emPurpose(emEtc) -> 기타가 공백이 아닐경우 emEtc 넣기
    // 2. 보낸사람 이름(이메일) : knName(insert_email)
    // 3. 발신자 이메일 : emEmailSend
    // 4. 제목 : emSendAllCount
    // 5. 이메일발송 전체건수 : emTitle
    // 6. 이메일발송 성공건수 : emSendSucCount
    // 7. 이메일발송 실패건수 : emSendFailCount
    // 8. 발송날짜 : send_date : emState 값이 발송예약중 일경우 예약취소버튼 추가 -> emId 파라메터로 하는 예약취소API호출

    private String emPurpose; // 발송목적(1:주요공지, 2:광고/홍보, 3:기타)

    private String emEtc; // 기타일 경우 해당 내용

    private String emTitle; // 제목

    private String emState; // 메일상태(1: 발송중, 2: 발송예약중, 3: 일부성공, 4: 발송실패, 5: 발송완료, 6: 발송취소)

    private Integer emSendAllCount; // 이메일발송 전체건수

    private Integer emSendSucCount; // 이메일발송 성공건수

    private Integer emSendFailCount; // 이메일발송 실패건수

    private String knName; // 보낸사람 이름

    private String insert_email; // 보낸사람 이메일

    private String emEmailSend; // 발신자 이메일

    private LocalDateTime send_date; // 발송날짜 예약 발송이 일경우 emReservationDate값을 보냄 그게아니면 insert_date : (emType로 비교)

    public Integer getEmSendAllCount() {
        return Objects.requireNonNullElse(emSendAllCount, 0);
    }

    public Integer getEmSendSucCount() {
        return Objects.requireNonNullElse(emSendSucCount, 0);
    }

    public Integer getEmSendFailCount() {
        return Objects.requireNonNullElse(emSendFailCount, 0);
    }

}
