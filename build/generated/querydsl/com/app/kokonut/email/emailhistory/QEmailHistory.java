package com.app.kokonut.email.emailhistory;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEmailHistory is a Querydsl query type for EmailHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailHistory extends EntityPathBase<EmailHistory> {

    private static final long serialVersionUID = -50727559L;

    public static final QEmailHistory emailHistory = new QEmailHistory("emailHistory");

    public final StringPath ehContents = createString("ehContents");

    public final StringPath ehFrom = createString("ehFrom");

    public final StringPath ehFromName = createString("ehFromName");

    public final NumberPath<Long> ehId = createNumber("ehId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> ehRegdate = createDateTime("ehRegdate", java.time.LocalDateTime.class);

    public final StringPath ehTitle = createString("ehTitle");

    public final StringPath ehTo = createString("ehTo");

    public final StringPath ehToName = createString("ehToName");

    public QEmailHistory(String variable) {
        super(EmailHistory.class, forVariable(variable));
    }

    public QEmailHistory(Path<? extends EmailHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmailHistory(PathMetadata metadata) {
        super(EmailHistory.class, metadata);
    }

}

