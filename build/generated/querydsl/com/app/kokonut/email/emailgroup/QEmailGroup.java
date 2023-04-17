package com.app.kokonut.email.emailgroup;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEmailGroup is a Querydsl query type for EmailGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailGroup extends EntityPathBase<EmailGroup> {

    private static final long serialVersionUID = 608868249L;

    public static final QEmailGroup emailGroup = new QEmailGroup("emailGroup");

    public final StringPath egAdminIdList = createString("egAdminIdList");

    public final StringPath egDesc = createString("egDesc");

    public final NumberPath<Long> egId = createNumber("egId", Long.class);

    public final StringPath egName = createString("egName");

    public final StringPath egUseYn = createString("egUseYn");

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public QEmailGroup(String variable) {
        super(EmailGroup.class, forVariable(variable));
    }

    public QEmailGroup(Path<? extends EmailGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmailGroup(PathMetadata metadata) {
        super(EmailGroup.class, metadata);
    }

}

