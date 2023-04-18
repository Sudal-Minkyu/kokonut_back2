package com.app.kokonut.revisedDocumentFile;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRevisedDocumentFile is a Querydsl query type for RevisedDocumentFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRevisedDocumentFile extends EntityPathBase<RevisedDocumentFile> {

    private static final long serialVersionUID = -1715321067L;

    public static final QRevisedDocumentFile revisedDocumentFile = new QRevisedDocumentFile("revisedDocumentFile");

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public final StringPath rdfFilename = createString("rdfFilename");

    public final NumberPath<Long> rdfId = createNumber("rdfId", Long.class);

    public final StringPath rdfOriginalFilename = createString("rdfOriginalFilename");

    public final StringPath rdfPath = createString("rdfPath");

    public final NumberPath<Long> rdfVolume = createNumber("rdfVolume", Long.class);

    public final NumberPath<Long> rdId = createNumber("rdId", Long.class);

    public QRevisedDocumentFile(String variable) {
        super(RevisedDocumentFile.class, forVariable(variable));
    }

    public QRevisedDocumentFile(Path<? extends RevisedDocumentFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRevisedDocumentFile(PathMetadata metadata) {
        super(RevisedDocumentFile.class, metadata);
    }

}

