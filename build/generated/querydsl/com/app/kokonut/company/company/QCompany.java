package com.app.kokonut.company.company;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCompany is a Querydsl query type for Company
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompany extends EntityPathBase<Company> {

    private static final long serialVersionUID = -388215656L;

    public static final QCompany company = new QCompany("company");

    public final NumberPath<Long> companyId = createNumber("companyId", Long.class);

    public final StringPath cpBillingKey = createString("cpBillingKey");

    public final StringPath cpCode = createString("cpCode");

    public final NumberPath<Integer> cpIsAutoPay = createNumber("cpIsAutoPay", Integer.class);

    public final StringPath cpName = createString("cpName");

    public final DateTimePath<java.time.LocalDateTime> cpNotAutoPayDate = createDateTime("cpNotAutoPayDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> cpPayDate = createDateTime("cpPayDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> cpPayDay = createNumber("cpPayDay", Integer.class);

    public final NumberPath<Integer> cpStopServicePrice = createNumber("cpStopServicePrice", Integer.class);

    public final NumberPath<Integer> cpTableCount = createNumber("cpTableCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> cpValidEnd = createDateTime("cpValidEnd", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> cpValidStart = createDateTime("cpValidStart", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public QCompany(String variable) {
        super(Company.class, forVariable(variable));
    }

    public QCompany(Path<? extends Company> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCompany(PathMetadata metadata) {
        super(Company.class, metadata);
    }

}

