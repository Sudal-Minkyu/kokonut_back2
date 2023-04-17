package com.app.kokonut.history;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHistory is a Querydsl query type for History
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHistory extends EntityPathBase<History> {

    private static final long serialVersionUID = -1373011401L;

    public static final QHistory history = new QHistory("history");

    public final EnumPath<com.app.kokonut.history.dto.ActivityCode> activityCode = createEnum("activityCode", com.app.kokonut.history.dto.ActivityCode.class);

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    public final StringPath ahActivityDetail = createString("ahActivityDetail");

    public final NumberPath<Long> ahId = createNumber("ahId", Long.class);

    public final StringPath ahIpAddr = createString("ahIpAddr");

    public final StringPath ahReason = createString("ahReason");

    public final NumberPath<Integer> ahState = createNumber("ahState", Integer.class);

    public final NumberPath<Integer> ahType = createNumber("ahType", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public QHistory(String variable) {
        super(History.class, forVariable(variable));
    }

    public QHistory(Path<? extends History> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHistory(PathMetadata metadata) {
        super(History.class, metadata);
    }

}

