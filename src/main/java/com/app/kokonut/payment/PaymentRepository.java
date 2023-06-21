package com.app.kokonut.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Woody
 * Date : 2023-06-07
 * Time :
 * Remark :
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment>, PaymentRepositoryCustom {

    List<Payment> findPaymentByPayStateAndPayMethod(String payState, String payMethod);

}