package com.app.kokonutapi.personalInfoProvision.personalInfoDownloadHistory;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPersonalInfoDownloadHistory is a Querydsl query type for PersonalInfoDownloadHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPersonalInfoDownloadHistory extends EntityPathBase<PersonalInfoDownloadHistory> {

    private static final long serialVersionUID = 672300008L;

    public static final QPersonalInfoDownloadHistory personalInfoDownloadHistory = new QPersonalInfoDownloadHistory("personalInfoDownloadHistory");

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final StringPath pdhAgreeYn = createString("pdhAgreeYn");

    public final StringPath pdhDestructionAgreeYn = createString("pdhDestructionAgreeYn");

    public final DateTimePath<java.time.LocalDateTime> pdhDestructionDate = createDateTime("pdhDestructionDate", java.time.LocalDateTime.class);

    public final StringPath pdhDestructionRegisterName = createString("pdhDestructionRegisterName");

    public final StringPath pdhEmail = createString("pdhEmail");

    public final StringPath pdhFileName = createString("pdhFileName");

    public final NumberPath<Long> pdhId = createNumber("pdhId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> pdhRetentionDate = createDateTime("pdhRetentionDate", java.time.LocalDateTime.class);

    public final StringPath piNumber = createString("piNumber");

    public QPersonalInfoDownloadHistory(String variable) {
        super(PersonalInfoDownloadHistory.class, forVariable(variable));
    }

    public QPersonalInfoDownloadHistory(Path<? extends PersonalInfoDownloadHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPersonalInfoDownloadHistory(PathMetadata metadata) {
        super(PersonalInfoDownloadHistory.class, metadata);
    }

}

