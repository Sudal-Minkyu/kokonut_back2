package com.app.kokonut.company.companytable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCompanyTable is a Querydsl query type for CompanyTable
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompanyTable extends EntityPathBase<CompanyTable> {

    private static final long serialVersionUID = -1349205894L;

    public static final QCompanyTable companyTable = new QCompanyTable("companyTable");

    public final StringPath cpCode = createString("cpCode");

    public final NumberPath<Integer> ctAddColumnCount = createNumber("ctAddColumnCount", Integer.class);

    public final StringPath ctDesignation = createString("ctDesignation");

    public final NumberPath<Long> ctId = createNumber("ctId", Long.class);

    public final StringPath ctName = createString("ctName");

    public final StringPath ctTableCount = createString("ctTableCount");

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public QCompanyTable(String variable) {
        super(CompanyTable.class, forVariable(variable));
    }

    public QCompanyTable(Path<? extends CompanyTable> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCompanyTable(PathMetadata metadata) {
        super(CompanyTable.class, metadata);
    }

}

