package com.app.kokonut.emailcontacthistory;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEmailContactHistory is a Querydsl query type for EmailContactHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailContactHistory extends EntityPathBase<EmailContactHistory> {

    private static final long serialVersionUID = 735147311L;

    public static final QEmailContactHistory emailContactHistory = new QEmailContactHistory("emailContactHistory");

    public final StringPath echContents = createString("echContents");

    public final StringPath echFrom = createString("echFrom");

    public final StringPath echFromName = createString("echFromName");

    public final NumberPath<Long> echId = createNumber("echId", Long.class);

    public final StringPath echTitle = createString("echTitle");

    public final StringPath echTo = createString("echTo");

    public final StringPath echToName = createString("echToName");

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public QEmailContactHistory(String variable) {
        super(EmailContactHistory.class, forVariable(variable));
    }

    public QEmailContactHistory(Path<? extends EmailContactHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmailContactHistory(PathMetadata metadata) {
        super(EmailContactHistory.class, metadata);
    }

}

