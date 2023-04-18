package com.app.kokonut.qnaFile;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QQnaFile is a Querydsl query type for QnaFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQnaFile extends EntityPathBase<QnaFile> {

    private static final long serialVersionUID = 808658959L;

    public static final QQnaFile qnaFile = new QQnaFile("qnaFile");

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public final StringPath qfBucket = createString("qfBucket");

    public final StringPath qfFilename = createString("qfFilename");

    public final NumberPath<Long> qfId = createNumber("qfId", Long.class);

    public final StringPath qfOriginalFilename = createString("qfOriginalFilename");

    public final StringPath qfPath = createString("qfPath");

    public final NumberPath<Long> qfVolume = createNumber("qfVolume", Long.class);

    public final NumberPath<Long> qnaId = createNumber("qnaId", Long.class);

    public QQnaFile(String variable) {
        super(QnaFile.class, forVariable(variable));
    }

    public QQnaFile(Path<? extends QnaFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQnaFile(PathMetadata metadata) {
        super(QnaFile.class, metadata);
    }

}

