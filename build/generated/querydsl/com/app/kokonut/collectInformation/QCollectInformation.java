package com.app.kokonut.collectInformation;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCollectInformation is a Querydsl query type for CollectInformation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCollectInformation extends EntityPathBase<CollectInformation> {

    private static final long serialVersionUID = -1220323605L;

    public static final QCollectInformation collectInformation = new QCollectInformation("collectInformation");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    public final StringPath ciContent = createString("ciContent");

    public final NumberPath<Long> ciId = createNumber("ciId", Long.class);

    public final StringPath ciTitle = createString("ciTitle");

    public final NumberPath<Long> companyId = createNumber("companyId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public final NumberPath<Long> modify_id = createNumber("modify_id", Long.class);

    public QCollectInformation(String variable) {
        super(CollectInformation.class, forVariable(variable));
    }

    public QCollectInformation(Path<? extends CollectInformation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCollectInformation(PathMetadata metadata) {
        super(CollectInformation.class, metadata);
    }

}

