package com.app.kokonut.refactor.policy;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPolicy is a Querydsl query type for Policy
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPolicy extends EntityPathBase<Policy> {

    private static final long serialVersionUID = 1652849189L;

    public static final QPolicy policy = new QPolicy("policy");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public final NumberPath<Long> modify_id = createNumber("modify_id", Long.class);

    public final DateTimePath<java.util.Date> plEffectiveDate = createDateTime("plEffectiveDate", java.util.Date.class);

    public final StringPath plHtml = createString("plHtml");

    public final NumberPath<Long> plId = createNumber("plId", Long.class);

    public final DateTimePath<java.util.Date> plRegistDate = createDateTime("plRegistDate", java.util.Date.class);

    public final NumberPath<Integer> plType = createNumber("plType", Integer.class);

    public QPolicy(String variable) {
        super(Policy.class, forVariable(variable));
    }

    public QPolicy(Path<? extends Policy> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPolicy(PathMetadata metadata) {
        super(Policy.class, metadata);
    }

}

