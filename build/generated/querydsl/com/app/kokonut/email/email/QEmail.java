package com.app.kokonut.email.email;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEmail is a Querydsl query type for Email
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmail extends EntityPathBase<Email> {

    private static final long serialVersionUID = -1941964491L;

    public static final QEmail email = new QEmail("email");

    public final NumberPath<Long> egId = createNumber("egId", Long.class);

    public final StringPath emContents = createString("emContents");

    public final NumberPath<Long> emId = createNumber("emId", Long.class);

    public final StringPath emReceiverAdminIdList = createString("emReceiverAdminIdList");

    public final StringPath emReceiverType = createString("emReceiverType");

    public final NumberPath<Long> emSenderAdminId = createNumber("emSenderAdminId", Long.class);

    public final StringPath emTitle = createString("emTitle");

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public QEmail(String variable) {
        super(Email.class, forVariable(variable));
    }

    public QEmail(Path<? extends Email> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmail(PathMetadata metadata) {
        super(Email.class, metadata);
    }

}

