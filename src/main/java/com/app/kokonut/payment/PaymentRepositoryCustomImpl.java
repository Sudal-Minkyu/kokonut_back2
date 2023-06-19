package com.app.kokonut.payment;

import com.app.kokonut.payment.dtos.PaymentListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * @author Woody
 * Date : 2023-06-07
 * Time :
 * Remark : PaymentRepositoryCustom 쿼리문 선언부
 */
@Repository
public class PaymentRepositoryCustomImpl extends QuerydslRepositorySupport implements PaymentRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public PaymentRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(Payment.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public Page<PaymentListDto> findPaymentPage(String cpCode, String authCode, Pageable pageable) {

        QPayment payment  = QPayment.payment;

        JPQLQuery<PaymentListDto> query = from(payment)
                .select(Projections.constructor(PaymentListDto.class,
                        payment.payId,
                        payment.payBillingStartDate,
                        payment.payBillingEndDate,
                        payment.payPrivacyCount,
                        payment.insert_date,
                        payment.payAmount,
                        payment.payState,
                        payment.payMethod
                ));

        if(!authCode.equals("ROLE_SYSTEM")) {
            // 시스템 관리자가 아닐경우 자신회사의 결제기록만 나와야함.
            query.where(payment.cpCode.eq(cpCode));
        }

        query.orderBy(payment.payId.desc());

        final List<PaymentListDto> PaymentListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(PaymentListDtos, pageable, query.fetchCount());
    }

//    private Long payId;
//
//    private LocalDateTime payBillingDate; // 요금부과 기간 시작 날짜 - 끝 날짜 -> 년.월.일 ~ 년.월.일(시작날짜 ~끝날짜) 로 표기
//
//    private String payPrivacyCount; // 결제일 기준 개인정보 평균수
//
//    private LocalDateTime insert_date; // 결제 일시
//
//    private Integer payAmount; // 결제금액
//
//    private String payState; // 결제상태(0:결제오류, 1:결제완료, 2:결제예약중)
//
//    private String payMethod; // 결제방법(0:자동결제, 1:요금정산, 2:결제실패)

}
