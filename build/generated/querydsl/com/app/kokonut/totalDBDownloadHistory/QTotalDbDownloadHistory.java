package com.app.kokonut.totalDBDownloadHistory;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTotalDbDownloadHistory is a Querydsl query type for TotalDbDownloadHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTotalDbDownloadHistory extends EntityPathBase<TotalDbDownloadHistory> {

    private static final long serialVersionUID = 1432625867L;

    public static final QTotalDbDownloadHistory totalDbDownloadHistory = new QTotalDbDownloadHistory("totalDbDownloadHistory");

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final StringPath tdhFileName = createString("tdhFileName");

    public final NumberPath<Long> tdhId = createNumber("tdhId", Long.class);

    public final StringPath tdhReason = createString("tdhReason");

    public final NumberPath<Long> tdId = createNumber("tdId", Long.class);

    public QTotalDbDownloadHistory(String variable) {
        super(TotalDbDownloadHistory.class, forVariable(variable));
    }

    public QTotalDbDownloadHistory(Path<? extends TotalDbDownloadHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTotalDbDownloadHistory(PathMetadata metadata) {
        super(TotalDbDownloadHistory.class, metadata);
    }

}

