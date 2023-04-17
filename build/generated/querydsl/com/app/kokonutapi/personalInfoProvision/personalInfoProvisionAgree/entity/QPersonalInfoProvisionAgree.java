package com.app.kokonutapi.personalInfoProvision.personalInfoProvisionAgree.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPersonalInfoProvisionAgree is a Querydsl query type for PersonalInfoProvisionAgree
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPersonalInfoProvisionAgree extends EntityPathBase<PersonalInfoProvisionAgree> {

    private static final long serialVersionUID = 1275900589L;

    public static final QPersonalInfoProvisionAgree personalInfoProvisionAgree = new QPersonalInfoProvisionAgree("personalInfoProvisionAgree");

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final StringPath piNumber = createString("piNumber");

    public final DateTimePath<java.time.LocalDateTime> ppaAgreeDate = createDateTime("ppaAgreeDate", java.time.LocalDateTime.class);

    public final StringPath ppaAgreeYn = createString("ppaAgreeYn");

    public final NumberPath<Long> ppaId = createNumber("ppaId", Long.class);

    public final StringPath userId = createString("userId");

    public QPersonalInfoProvisionAgree(String variable) {
        super(PersonalInfoProvisionAgree.class, forVariable(variable));
    }

    public QPersonalInfoProvisionAgree(Path<? extends PersonalInfoProvisionAgree> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPersonalInfoProvisionAgree(PathMetadata metadata) {
        super(PersonalInfoProvisionAgree.class, metadata);
    }

}

