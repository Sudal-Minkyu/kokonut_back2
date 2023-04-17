package com.app.kokonut.bizMessage.alimtalkMessage;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAlimtalkMessage is a Querydsl query type for AlimtalkMessage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlimtalkMessage extends EntityPathBase<AlimtalkMessage> {

    private static final long serialVersionUID = -1077499949L;

    public static final QAlimtalkMessage alimtalkMessage = new QAlimtalkMessage("alimtalkMessage");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    public final NumberPath<Long> amId = createNumber("amId", Long.class);

    public final StringPath amRequestId = createString("amRequestId");

    public final DateTimePath<java.time.LocalDateTime> amReservationDate = createDateTime("amReservationDate", java.time.LocalDateTime.class);

    public final StringPath amStatus = createString("amStatus");

    public final StringPath amTransmitType = createString("amTransmitType");

    public final StringPath atTemplateCode = createString("atTemplateCode");

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final StringPath kcChannelId = createString("kcChannelId");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public QAlimtalkMessage(String variable) {
        super(AlimtalkMessage.class, forVariable(variable));
    }

    public QAlimtalkMessage(Path<? extends AlimtalkMessage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAlimtalkMessage(PathMetadata metadata) {
        super(AlimtalkMessage.class, metadata);
    }

}

