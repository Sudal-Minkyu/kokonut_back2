package com.app.kokonut.service;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QKnService is a Querydsl query type for KnService
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKnService extends EntityPathBase<KnService> {

    private static final long serialVersionUID = -2040710762L;

    public static final QKnService knService = new QKnService("knService");

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public final NumberPath<Long> srId = createNumber("srId", Long.class);

    public final NumberPath<Integer> srPerPrice = createNumber("srPerPrice", Integer.class);

    public final NumberPath<Integer> srPrice = createNumber("srPrice", Integer.class);

    public final StringPath srService = createString("srService");

    public QKnService(String variable) {
        super(KnService.class, forVariable(variable));
    }

    public QKnService(Path<? extends KnService> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKnService(PathMetadata metadata) {
        super(KnService.class, metadata);
    }

}

