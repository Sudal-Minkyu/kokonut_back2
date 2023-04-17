package com.app.kokonut.refactor.privacyEmailHistory;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPrivacyEmailHistory is a Querydsl query type for PrivacyEmailHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPrivacyEmailHistory extends EntityPathBase<PrivacyEmailHistory> {

    private static final long serialVersionUID = -1035559735L;

    public static final QPrivacyEmailHistory privacyEmailHistory = new QPrivacyEmailHistory("privacyEmailHistory");

    public final NumberPath<Long> peId = createNumber("peId", Long.class);

    public final NumberPath<Long> phId = createNumber("phId", Long.class);

    public final StringPath phReceiverEmail = createString("phReceiverEmail");

    public final DateTimePath<java.util.Date> phSendDate = createDateTime("phSendDate", java.util.Date.class);

    public QPrivacyEmailHistory(String variable) {
        super(PrivacyEmailHistory.class, forVariable(variable));
    }

    public QPrivacyEmailHistory(Path<? extends PrivacyEmailHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPrivacyEmailHistory(PathMetadata metadata) {
        super(PrivacyEmailHistory.class, metadata);
    }

}

