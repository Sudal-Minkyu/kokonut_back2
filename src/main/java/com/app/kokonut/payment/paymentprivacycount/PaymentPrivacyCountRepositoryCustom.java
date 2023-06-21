package com.app.kokonut.payment.paymentprivacycount;

import com.app.kokonut.payment.paymentprivacycount.dtos.PaymentPrivacyCountDayDto;
import com.app.kokonut.payment.paymentprivacycount.dtos.PaymentPrivacyCountMonthAverageDto;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Woody
 * Date : 2023-06-20
 * Time :
 * Remark : PaymentPrivacy Sql 쿼리호출
 */
public interface PaymentPrivacyCountRepositoryCustom {

    PaymentPrivacyCountMonthAverageDto findByMonthPrivacyCount(String cpCode, String ctName, LocalDate firstDate, LocalDate lastDate); // 월평균 개인정보 수 호출

    List<PaymentPrivacyCountDayDto> findByDayPrivacyCount(LocalDate firstDate, LocalDate lastDate);

}