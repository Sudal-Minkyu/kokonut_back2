package com.app.kokonut.company.companydatakey;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCompanyDataKey is a Querydsl query type for CompanyDataKey
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompanyDataKey extends EntityPathBase<CompanyDataKey> {

    private static final long serialVersionUID = 1178354746L;

    public static final QCompanyDataKey companyDataKey = new QCompanyDataKey("companyDataKey");

    public final NumberPath<Long> cdId = createNumber("cdId", Long.class);

    public final StringPath cpCode = createString("cpCode");

    public final StringPath dataKey = createString("dataKey");

    public QCompanyDataKey(String variable) {
        super(CompanyDataKey.class, forVariable(variable));
    }

    public QCompanyDataKey(Path<? extends CompanyDataKey> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCompanyDataKey(PathMetadata metadata) {
        super(CompanyDataKey.class, metadata);
    }

}

