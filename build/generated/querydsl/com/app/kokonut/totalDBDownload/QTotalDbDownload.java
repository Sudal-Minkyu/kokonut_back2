package com.app.kokonut.totalDBDownload;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTotalDbDownload is a Querydsl query type for TotalDbDownload
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTotalDbDownload extends EntityPathBase<TotalDbDownload> {

    private static final long serialVersionUID = -1639036925L;

    public static final QTotalDbDownload totalDbDownload = new QTotalDbDownload("totalDbDownload");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> tdApplyDate = createDateTime("tdApplyDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> tdDownloadDate = createDateTime("tdDownloadDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> tdId = createNumber("tdId", Long.class);

    public final StringPath tdIpAddr = createString("tdIpAddr");

    public final NumberPath<Integer> tdLimit = createNumber("tdLimit", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> tdLimitDateEnd = createDateTime("tdLimitDateEnd", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> tdLimitDateStart = createDateTime("tdLimitDateStart", java.time.LocalDateTime.class);

    public final StringPath tdLink = createString("tdLink");

    public final StringPath tdReason = createString("tdReason");

    public final StringPath tdReturnReason = createString("tdReturnReason");

    public final NumberPath<Integer> tdState = createNumber("tdState", Integer.class);

    public QTotalDbDownload(String variable) {
        super(TotalDbDownload.class, forVariable(variable));
    }

    public QTotalDbDownload(Path<? extends TotalDbDownload> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTotalDbDownload(PathMetadata metadata) {
        super(TotalDbDownload.class, metadata);
    }

}

