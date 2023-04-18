package com.app.kokonut.inquiry;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QInquiry is a Querydsl query type for Inquiry
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInquiry extends EntityPathBase<Inquiry> {

    private static final long serialVersionUID = 1543235997L;

    public static final QInquiry inquiry = new QInquiry("inquiry");

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath iqContents = createString("iqContents");

    public final StringPath iqEmail = createString("iqEmail");

    public final NumberPath<Integer> iqField = createNumber("iqField", Integer.class);

    public final StringPath iqGroup = createString("iqGroup");

    public final NumberPath<Long> iqId = createNumber("iqId", Long.class);

    public final NumberPath<Integer> iqState = createNumber("iqState", Integer.class);

    public final StringPath iqTitle = createString("iqTitle");

    public final StringPath iqWriter = createString("iqWriter");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public QInquiry(String variable) {
        super(Inquiry.class, forVariable(variable));
    }

    public QInquiry(Path<? extends Inquiry> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInquiry(PathMetadata metadata) {
        super(Inquiry.class, metadata);
    }

}

