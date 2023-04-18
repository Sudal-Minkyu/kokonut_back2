package com.app.kokonut.refactor.privacyEmail;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPrivacyEmail is a Querydsl query type for PrivacyEmail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPrivacyEmail extends EntityPathBase<PrivacyEmail> {

    private static final long serialVersionUID = -16857627L;

    public static final QPrivacyEmail privacyEmail = new QPrivacyEmail("privacyEmail");

    public final NumberPath<Long> companyId = createNumber("companyId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public final StringPath peContents = createString("peContents");

    public final NumberPath<Integer> peId = createNumber("peId", Integer.class);

    public final StringPath peSenderEmail = createString("peSenderEmail");

    public final StringPath peTitle = createString("peTitle");

    public QPrivacyEmail(String variable) {
        super(PrivacyEmail.class, forVariable(variable));
    }

    public QPrivacyEmail(Path<? extends PrivacyEmail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPrivacyEmail(PathMetadata metadata) {
        super(PrivacyEmail.class, metadata);
    }

}

