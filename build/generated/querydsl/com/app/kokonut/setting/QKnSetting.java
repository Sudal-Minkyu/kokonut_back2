package com.app.kokonut.setting;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QKnSetting is a Querydsl query type for KnSetting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKnSetting extends EntityPathBase<KnSetting> {

    private static final long serialVersionUID = -186694900L;

    public static final QKnSetting knSetting = new QKnSetting("knSetting");

    public final NumberPath<Long> companyId = createNumber("companyId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public final NumberPath<Integer> stDormantAccount = createNumber("stDormantAccount", Integer.class);

    public final NumberPath<Long> stId = createNumber("stId", Long.class);

    public final NumberPath<Integer> stOverseasBlock = createNumber("stOverseasBlock", Integer.class);

    public QKnSetting(String variable) {
        super(KnSetting.class, forVariable(variable));
    }

    public QKnSetting(Path<? extends KnSetting> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKnSetting(PathMetadata metadata) {
        super(KnSetting.class, metadata);
    }

}

