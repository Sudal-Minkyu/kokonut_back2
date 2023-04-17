package com.app.kokonut.revisedDocument;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRevisedDocument is a Querydsl query type for RevisedDocument
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRevisedDocument extends EntityPathBase<RevisedDocument> {

    private static final long serialVersionUID = -1996631459L;

    public static final QRevisedDocument revisedDocument = new QRevisedDocument("revisedDocument");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public final DateTimePath<java.time.LocalDateTime> rdEnforceEndDate = createDateTime("rdEnforceEndDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> rdEnforceStartDate = createDateTime("rdEnforceStartDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> rdId = createNumber("rdId", Long.class);

    public QRevisedDocument(String variable) {
        super(RevisedDocument.class, forVariable(variable));
    }

    public QRevisedDocument(Path<? extends RevisedDocument> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRevisedDocument(PathMetadata metadata) {
        super(RevisedDocument.class, metadata);
    }

}

