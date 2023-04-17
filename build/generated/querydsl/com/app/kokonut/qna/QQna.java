package com.app.kokonut.qna;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QQna is a Querydsl query type for Qna
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQna extends EntityPathBase<Qna> {

    private static final long serialVersionUID = -637362985L;

    public static final QQna qna = new QQna("qna");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public final NumberPath<Long> modify_id = createNumber("modify_id", Long.class);

    public final StringPath qnaAnswer = createString("qnaAnswer");

    public final StringPath qnaContent = createString("qnaContent");

    public final NumberPath<Long> qnaId = createNumber("qnaId", Long.class);

    public final NumberPath<Integer> qnaState = createNumber("qnaState", Integer.class);

    public final StringPath qnaTitle = createString("qnaTitle");

    public final NumberPath<Integer> qnaType = createNumber("qnaType", Integer.class);

    public QQna(String variable) {
        super(Qna.class, forVariable(variable));
    }

    public QQna(Path<? extends Qna> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQna(PathMetadata metadata) {
        super(Qna.class, metadata);
    }

}

