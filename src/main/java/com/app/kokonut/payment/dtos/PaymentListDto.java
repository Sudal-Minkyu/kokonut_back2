package com.app.kokonut.payment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Woody
 * Date : 2023-06-14
 * Time :
 * Remark : 구독관리 결제 ListDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentListDto {

    private Long payId;

    private LocalDate payBillingStartDate; // 요금부과 기간 시작 날짜

    private LocalDate payBillingEndDate; // 요금부과 기간 끝 날짜

    private Integer payPrivacyCount; // 결제일 기준 개인정보 평균수

    private LocalDateTime payReserveExecuteDate; // 결제 일시

    private Integer payAmount; // 결제금액

    private String payState; // 결제상태(0:결제오류, 1:결제완료, 2:결제예약중)

    private String payMethod; // 결제방법(0:자동결제, 1:요금정산, 2:결제실패)

    public String getPayBillingStartDate() {
        return DateTimeFormatter.ofPattern("yyyy.MM.dd").format(payBillingStartDate);
    }

    public String getPayBillingEndDate() {
        return DateTimeFormatter.ofPattern("yyyy.MM.dd").format(payBillingEndDate);
    }

    public String getPayReserveExecuteDate() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(payReserveExecuteDate);
    }


}
