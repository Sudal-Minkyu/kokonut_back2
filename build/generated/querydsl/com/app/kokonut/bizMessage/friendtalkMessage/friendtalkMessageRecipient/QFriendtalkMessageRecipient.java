package com.app.kokonut.bizMessage.friendtalkMessage.friendtalkMessageRecipient;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFriendtalkMessageRecipient is a Querydsl query type for FriendtalkMessageRecipient
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFriendtalkMessageRecipient extends EntityPathBase<FriendtalkMessageRecipient> {

    private static final long serialVersionUID = -1714343994L;

    public static final QFriendtalkMessageRecipient friendtalkMessageRecipient = new QFriendtalkMessageRecipient("friendtalkMessageRecipient");

    public final NumberPath<Long> fmId = createNumber("fmId", Long.class);

    public final StringPath fmrEmail = createString("fmrEmail");

    public final NumberPath<Long> fmrId = createNumber("fmrId", Long.class);

    public final StringPath fmrName = createString("fmrName");

    public final StringPath fmrPhoneNumber = createString("fmrPhoneNumber");

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public QFriendtalkMessageRecipient(String variable) {
        super(FriendtalkMessageRecipient.class, forVariable(variable));
    }

    public QFriendtalkMessageRecipient(Path<? extends FriendtalkMessageRecipient> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFriendtalkMessageRecipient(PathMetadata metadata) {
        super(FriendtalkMessageRecipient.class, metadata);
    }

}

