package com.app.kokonut.payment.paymentprivacycount.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author Woody
 * Date : 2023-06-20
 * Time :
 * Remark : 월단위 일일 개인정보 수 반환 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPrivacyCountDayDto {

    private LocalDate ppcDate;

    private Integer ppcCount;

    public String getPpcDate() {
        return String.valueOf(ppcDate.getDayOfMonth());
    }

}
