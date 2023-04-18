package com.app.kokonutapi.personalInfoProvision;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPersonalInfoProvision is a Querydsl query type for PersonalInfoProvision
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPersonalInfoProvision extends EntityPathBase<PersonalInfoProvision> {

    private static final long serialVersionUID = 1042060737L;

    public static final QPersonalInfoProvision personalInfoProvision = new QPersonalInfoProvision("personalInfoProvision");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public final NumberPath<Integer> piAgreeType = createNumber("piAgreeType", Integer.class);

    public final StringPath piAgreeYn = createString("piAgreeYn");

    public final StringPath piColumns = createString("piColumns");

    public final DateTimePath<java.time.LocalDateTime> piExpDate = createDateTime("piExpDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> piId = createNumber("piId", Long.class);

    public final StringPath piNumber = createString("piNumber");

    public final StringPath piPurpose = createString("piPurpose");

    public final NumberPath<Integer> piReason = createNumber("piReason", Integer.class);

    public final StringPath piRecipientEmail = createString("piRecipientEmail");

    public final NumberPath<Integer> piRecipientType = createNumber("piRecipientType", Integer.class);

    public final StringPath piRetentionPeriod = createString("piRetentionPeriod");

    public final DateTimePath<java.time.LocalDateTime> piStartDate = createDateTime("piStartDate", java.time.LocalDateTime.class);

    public final StringPath piTag = createString("piTag");

    public final StringPath piTargets = createString("piTargets");

    public final StringPath piTargetStatus = createString("piTargetStatus");

    public final NumberPath<Integer> piType = createNumber("piType", Integer.class);

    public QPersonalInfoProvision(String variable) {
        super(PersonalInfoProvision.class, forVariable(variable));
    }

    public QPersonalInfoProvision(Path<? extends PersonalInfoProvision> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPersonalInfoProvision(PathMetadata metadata) {
        super(PersonalInfoProvision.class, metadata);
    }

}

