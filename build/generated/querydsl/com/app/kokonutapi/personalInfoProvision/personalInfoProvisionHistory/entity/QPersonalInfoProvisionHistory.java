package com.app.kokonutapi.personalInfoProvision.personalInfoProvisionHistory.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPersonalInfoProvisionHistory is a Querydsl query type for PersonalInfoProvisionHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPersonalInfoProvisionHistory extends EntityPathBase<PersonalInfoProvisionHistory> {

    private static final long serialVersionUID = 86145661L;

    public static final QPersonalInfoProvisionHistory personalInfoProvisionHistory = new QPersonalInfoProvisionHistory("personalInfoProvisionHistory");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    public final NumberPath<Long> companyId = createNumber("companyId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public final StringPath piNumber = createString("piNumber");

    public final NumberPath<Long> pphId = createNumber("pphId", Long.class);

    public QPersonalInfoProvisionHistory(String variable) {
        super(PersonalInfoProvisionHistory.class, forVariable(variable));
    }

    public QPersonalInfoProvisionHistory(Path<? extends PersonalInfoProvisionHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPersonalInfoProvisionHistory(PathMetadata metadata) {
        super(PersonalInfoProvisionHistory.class, metadata);
    }

}

