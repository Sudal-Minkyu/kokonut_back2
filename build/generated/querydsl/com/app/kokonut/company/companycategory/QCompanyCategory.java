package com.app.kokonut.company.companycategory;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCompanyCategory is a Querydsl query type for CompanyCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompanyCategory extends EntityPathBase<CompanyCategory> {

    private static final long serialVersionUID = -1355080268L;

    public static final QCompanyCategory companyCategory = new QCompanyCategory("companyCategory");

    public final NumberPath<Long> ccId = createNumber("ccId", Long.class);

    public final StringPath ccName = createString("ccName");

    public final StringPath ccSecurity = createString("ccSecurity");

    public final StringPath cpCode = createString("cpCode");

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public QCompanyCategory(String variable) {
        super(CompanyCategory.class, forVariable(variable));
    }

    public QCompanyCategory(Path<? extends CompanyCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCompanyCategory(PathMetadata metadata) {
        super(CompanyCategory.class, metadata);
    }

}

