package com.app.kokonut.bizMessage.alimtalkMessage.alimtalkMessageRecipient;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlimtalkMessageRecipient is a Querydsl query type for AlimtalkMessageRecipient
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlimtalkMessageRecipient extends EntityPathBase<AlimtalkMessageRecipient> {

    private static final long serialVersionUID = 2008137045L;

    public static final QAlimtalkMessageRecipient alimtalkMessageRecipient = new QAlimtalkMessageRecipient("alimtalkMessageRecipient");

    public final NumberPath<Long> amId = createNumber("amId", Long.class);

    public final StringPath amr_email = createString("amr_email");

    public final StringPath amr_name = createString("amr_name");

    public final StringPath amr_phone_number = createString("amr_phone_number");

    public final NumberPath<Long> amrId = createNumber("amrId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public QAlimtalkMessageRecipient(String variable) {
        super(AlimtalkMessageRecipient.class, forVariable(variable));
    }

    public QAlimtalkMessageRecipient(Path<? extends AlimtalkMessageRecipient> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlimtalkMessageRecipient(PathMetadata metadata) {
        super(AlimtalkMessageRecipient.class, metadata);
    }

}

