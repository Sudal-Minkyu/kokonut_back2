package com.app.kokonut.refactor.payment;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPayment is a Querydsl query type for Payment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPayment extends EntityPathBase<Payment> {

    private static final long serialVersionUID = -1538613035L;

    public static final QPayment payment = new QPayment("payment");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final StringPath applyRefund = createString("applyRefund");

    public final StringPath cardName = createString("cardName");

    public final StringPath cardNumber = createString("cardNumber");

    public final NumberPath<Long> companyId = createNumber("companyId", Long.class);

    public final NumberPath<Integer> idx = createNumber("idx", Integer.class);

    public final StringPath impUid = createString("impUid");

    public final StringPath merchantUid = createString("merchantUid");

    public final StringPath payMethod = createString("payMethod");

    public final StringPath payRequestUid = createString("payRequestUid");

    public final StringPath pgTid = createString("pgTid");

    public final StringPath receiptUrl = createString("receiptUrl");

    public final DateTimePath<java.util.Date> refundApplyDate = createDateTime("refundApplyDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> refundDate = createDateTime("refundDate", java.util.Date.class);

    public final StringPath refundReason = createString("refundReason");

    public final StringPath refundState = createString("refundState");

    public final DateTimePath<java.util.Date> regdate = createDateTime("regdate", java.util.Date.class);

    public final StringPath service = createString("service");

    public final NumberPath<Integer> state = createNumber("state", Integer.class);

    public final NumberPath<Integer> userCount = createNumber("userCount", Integer.class);

    public final DateTimePath<java.util.Date> validEnd = createDateTime("validEnd", java.util.Date.class);

    public final DateTimePath<java.util.Date> validStart = createDateTime("validStart", java.util.Date.class);

    public QPayment(String variable) {
        super(Payment.class, forVariable(variable));
    }

    public QPayment(Path<? extends Payment> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPayment(PathMetadata metadata) {
        super(Payment.class, metadata);
    }

}

