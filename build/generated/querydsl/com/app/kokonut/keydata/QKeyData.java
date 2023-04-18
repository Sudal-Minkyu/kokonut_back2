package com.app.kokonut.keydata;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QKeyData is a Querydsl query type for KeyData
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKeyData extends EntityPathBase<KeyData> {

    private static final long serialVersionUID = -973015743L;

    public static final QKeyData keyData = new QKeyData("keyData");

    public final StringPath kdKeyDescription = createString("kdKeyDescription");

    public final StringPath kdKeyGroup = createString("kdKeyGroup");

    public final StringPath kdKeyName = createString("kdKeyName");

    public final StringPath kdKeyValue = createString("kdKeyValue");

    public QKeyData(String variable) {
        super(KeyData.class, forVariable(variable));
    }

    public QKeyData(Path<? extends KeyData> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKeyData(PathMetadata metadata) {
        super(KeyData.class, metadata);
    }

}

