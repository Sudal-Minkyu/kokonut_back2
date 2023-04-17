package com.app.kokonut.bizMessage.friendtalkMessage;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFriendtalkMessage is a Querydsl query type for FriendtalkMessage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFriendtalkMessage extends EntityPathBase<FriendtalkMessage> {

    private static final long serialVersionUID = 720896693L;

    public static final QFriendtalkMessage friendtalkMessage = new QFriendtalkMessage("friendtalkMessage");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    public final NumberPath<Long> companyId = createNumber("companyId", Long.class);

    public final NumberPath<Long> fmId = createNumber("fmId", Long.class);

    public final StringPath fmRequestId = createString("fmRequestId");

    public final DateTimePath<java.time.LocalDateTime> fmReservationDate = createDateTime("fmReservationDate", java.time.LocalDateTime.class);

    public final StringPath fmStatus = createString("fmStatus");

    public final StringPath fmTransmitType = createString("fmTransmitType");

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final StringPath kcChannelId = createString("kcChannelId");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public QFriendtalkMessage(String variable) {
        super(FriendtalkMessage.class, forVariable(variable));
    }

    public QFriendtalkMessage(Path<? extends FriendtalkMessage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFriendtalkMessage(PathMetadata metadata) {
        super(FriendtalkMessage.class, metadata);
    }

}

