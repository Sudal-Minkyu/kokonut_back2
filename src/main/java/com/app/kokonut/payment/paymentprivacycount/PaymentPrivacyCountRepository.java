package com.app.kokonut.payment.paymentprivacycount;

import com.app.kokonut.payment.Payment;
import com.app.kokonut.payment.PaymentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-06-17
 * Time :
 * Remark :
 */
@Repository
public interface PaymentPrivacyCountRepository extends JpaRepository<PaymentPrivacyCount, Long>, JpaSpecificationExecutor<PaymentPrivacyCount> {

}