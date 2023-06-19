package com.app.kokonut.payment;

import com.app.kokonut.payment.dtos.PaymentListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Woody
 * Date : 2023-06-07
 * Time :
 * Remark : Payment Sql 쿼리호출
 */
public interface PaymentRepositoryCustom {

    Page<PaymentListDto> findPaymentPage(String cpCode, String authCode, Pageable pageable);

}