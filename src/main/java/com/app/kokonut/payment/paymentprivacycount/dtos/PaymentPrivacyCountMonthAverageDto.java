package com.app.kokonut.payment.paymentprivacycount.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;

/**
 * @author Woody
 * Date : 2023-06-20
 * Time :
 * Remark : 월 평균 개인정보 수 반환 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPrivacyCountMonthAverageDto {

    private Date lowDate;

    private Date bigDate;

    private BigInteger monthAverageCount; // 월 평균 개인정보 수

    public LocalDate getLowDate() {
        return lowDate.toLocalDate();
    }

    public LocalDate getBigDate() {
        return bigDate.toLocalDate();
    }

    public Integer getMonthAverageCount() {
        return Integer.parseInt(String.valueOf(monthAverageCount));
    }

}
