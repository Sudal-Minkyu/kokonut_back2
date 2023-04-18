package com.app.kokonut.company.companyFile;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCompanyFile is a Querydsl query type for CompanyFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompanyFile extends EntityPathBase<CompanyFile> {

    private static final long serialVersionUID = -608596656L;

    public static final QCompanyFile companyFile = new QCompanyFile("companyFile");

    public final StringPath cfFilename = createString("cfFilename");

    public final NumberPath<Long> cfId = createNumber("cfId", Long.class);

    public final StringPath cfOriginalFilename = createString("cfOriginalFilename");

    public final StringPath cfPath = createString("cfPath");

    public final NumberPath<Long> cfVolume = createNumber("cfVolume", Long.class);

    public final StringPath cpCode = createString("cpCode");

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public QCompanyFile(String variable) {
        super(CompanyFile.class, forVariable(variable));
    }

    public QCompanyFile(Path<? extends CompanyFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCompanyFile(PathMetadata metadata) {
        super(CompanyFile.class, metadata);
    }

}

