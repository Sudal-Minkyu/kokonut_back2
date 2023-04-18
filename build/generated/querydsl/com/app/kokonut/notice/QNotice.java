package com.app.kokonut.notice;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNotice is a Querydsl query type for Notice
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotice extends EntityPathBase<Notice> {

    private static final long serialVersionUID = -411289813L;

    public static final QNotice notice = new QNotice("notice");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public final NumberPath<Long> modify_id = createNumber("modify_id", Long.class);

    public final StringPath ntContent = createString("ntContent");

    public final NumberPath<Long> ntId = createNumber("ntId", Long.class);

    public final NumberPath<Integer> ntIsNotice = createNumber("ntIsNotice", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> ntRegistDate = createDateTime("ntRegistDate", java.time.LocalDateTime.class);

    public final StringPath ntRegisterName = createString("ntRegisterName");

    public final NumberPath<Integer> ntState = createNumber("ntState", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> ntStopDate = createDateTime("ntStopDate", java.time.LocalDateTime.class);

    public final StringPath ntTitle = createString("ntTitle");

    public final NumberPath<Integer> ntViewCount = createNumber("ntViewCount", Integer.class);

    public QNotice(String variable) {
        super(Notice.class, forVariable(variable));
    }

    public QNotice(Path<? extends Notice> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNotice(PathMetadata metadata) {
        super(Notice.class, metadata);
    }

}

