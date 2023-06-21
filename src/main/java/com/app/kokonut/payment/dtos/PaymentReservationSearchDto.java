package com.app.kokonut.payment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReservationSearchDto {

    private Integer status; // 0 : 예약결제 전 대기 상태, 1 : 예약결제 완료, 3 : 예약결제 취소상태, -1 : 예약결제 실패

    private String payReceiptid;

    private LocalDateTime payReserveStartedDate;

    private LocalDateTime payReserveFinishedDate;

}
