package com.app.kokonut.awsKmsHistory;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAwsKmsHistory is a Querydsl query type for AwsKmsHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAwsKmsHistory extends EntityPathBase<AwsKmsHistory> {

    private static final long serialVersionUID = 1415471567L;

    public static final QAwsKmsHistory awsKmsHistory = new QAwsKmsHistory("awsKmsHistory");

    public final NumberPath<Long> akhIdx = createNumber("akhIdx", Long.class);

    public final DateTimePath<java.time.LocalDateTime> akhRegdate = createDateTime("akhRegdate", java.time.LocalDateTime.class);

    public final StringPath akhType = createString("akhType");

    public QAwsKmsHistory(String variable) {
        super(AwsKmsHistory.class, forVariable(variable));
    }

    public QAwsKmsHistory(Path<? extends AwsKmsHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAwsKmsHistory(PathMetadata metadata) {
        super(AwsKmsHistory.class, metadata);
    }

}

