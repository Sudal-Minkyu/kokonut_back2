package com.app.kokonut.apiKey;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QApiKey is a Querydsl query type for ApiKey
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QApiKey extends EntityPathBase<ApiKey> {

    private static final long serialVersionUID = -65954165L;

    public static final QApiKey apiKey = new QApiKey("apiKey");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    public final StringPath akAgreeIp1 = createString("akAgreeIp1");

    public final StringPath akAgreeIp2 = createString("akAgreeIp2");

    public final StringPath akAgreeIp3 = createString("akAgreeIp3");

    public final StringPath akAgreeIp4 = createString("akAgreeIp4");

    public final StringPath akAgreeIp5 = createString("akAgreeIp5");

    public final StringPath akAgreeMemo1 = createString("akAgreeMemo1");

    public final StringPath akAgreeMemo2 = createString("akAgreeMemo2");

    public final StringPath akAgreeMemo3 = createString("akAgreeMemo3");

    public final StringPath akAgreeMemo4 = createString("akAgreeMemo4");

    public final StringPath akAgreeMemo5 = createString("akAgreeMemo5");

    public final NumberPath<Long> akId = createNumber("akId", Long.class);

    public final StringPath akKey = createString("akKey");

    public final StringPath akReason = createString("akReason");

    public final StringPath akUseYn = createString("akUseYn");

    public final NumberPath<Long> companyId = createNumber("companyId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public QApiKey(String variable) {
        super(ApiKey.class, forVariable(variable));
    }

    public QApiKey(Path<? extends ApiKey> path) {
        super(path.getType(), path.getMetadata());
    }

    public QApiKey(PathMetadata metadata) {
        super(ApiKey.class, metadata);
    }

}

