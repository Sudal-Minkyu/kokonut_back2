package com.app.kokonut.adminRemove;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAdminRemove is a Querydsl query type for AdminRemove
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdminRemove extends EntityPathBase<AdminRemove> {

    private static final long serialVersionUID = -653442059L;

    public static final QAdminRemove adminRemove = new QAdminRemove("adminRemove");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    public final StringPath ar_reason_detail = createString("ar_reason_detail");

    public final StringPath arEmail = createString("arEmail");

    public final NumberPath<Integer> arReason = createNumber("arReason", Integer.class);

    public final DateTimePath<java.util.Date> arWithdrawalDate = createDateTime("arWithdrawalDate", java.util.Date.class);

    public QAdminRemove(String variable) {
        super(AdminRemove.class, forVariable(variable));
    }

    public QAdminRemove(Path<? extends AdminRemove> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAdminRemove(PathMetadata metadata) {
        super(AdminRemove.class, metadata);
    }

}

