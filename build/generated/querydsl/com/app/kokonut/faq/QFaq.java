package com.app.kokonut.faq;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFaq is a Querydsl query type for Faq
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFaq extends EntityPathBase<Faq> {

    private static final long serialVersionUID = 2127584827L;

    public static final QFaq faq = new QFaq("faq");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    public final StringPath faqAnswer = createString("faqAnswer");

    public final NumberPath<Long> faqId = createNumber("faqId", Long.class);

    public final StringPath faqQuestion = createString("faqQuestion");

    public final DateTimePath<java.time.LocalDateTime> faqRegistEndDate = createDateTime("faqRegistEndDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> faqRegistStartDate = createDateTime("faqRegistStartDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> faqState = createNumber("faqState", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> faqStopDate = createDateTime("faqStopDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> faqType = createNumber("faqType", Integer.class);

    public final NumberPath<Integer> faqViewCount = createNumber("faqViewCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public final NumberPath<Long> modify_id = createNumber("modify_id", Long.class);

    public QFaq(String variable) {
        super(Faq.class, forVariable(variable));
    }

    public QFaq(Path<? extends Faq> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFaq(PathMetadata metadata) {
        super(Faq.class, metadata);
    }

}

