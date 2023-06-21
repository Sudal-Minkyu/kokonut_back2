package com.app.kokonut.payment.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-06-20
 * Time :
 * Remark : 결제금액 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReservationResultDto {

    private String payReserveId; // 예약 고유ID

    private LocalDateTime payReserveExecuteDate; // 예약 결제 시간

}
