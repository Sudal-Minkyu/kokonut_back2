package com.app.kokonut.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAdmin is a Querydsl query type for Admin
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdmin extends EntityPathBase<Admin> {

    private static final long serialVersionUID = 1164314477L;

    public static final QAdmin admin = new QAdmin("admin");

    public final NumberPath<Long> adminId = createNumber("adminId", Long.class);

    public final NumberPath<Long> companyId = createNumber("companyId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final StringPath insert_email = createString("insert_email");

    public final DateTimePath<java.time.LocalDateTime> knApprovalDate = createDateTime("knApprovalDate", java.time.LocalDateTime.class);

    public final StringPath knApprovalName = createString("knApprovalName");

    public final StringPath knApprovalReturnReason = createString("knApprovalReturnReason");

    public final NumberPath<Integer> knApprovalState = createNumber("knApprovalState", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> knAuthEndDate = createDateTime("knAuthEndDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> knAuthStartDate = createDateTime("knAuthStartDate", java.time.LocalDateTime.class);

    public final StringPath knDepartment = createString("knDepartment");

    public final DateTimePath<java.time.LocalDateTime> knDormantDate = createDateTime("knDormantDate", java.time.LocalDateTime.class);

    public final StringPath knEmail = createString("knEmail");

    public final StringPath knEmailAuthCode = createString("knEmailAuthCode");

    public final StringPath knEmailAuthNumber = createString("knEmailAuthNumber");

    public final DateTimePath<java.time.LocalDateTime> knExpectedDeleteDate = createDateTime("knExpectedDeleteDate", java.time.LocalDateTime.class);

    public final StringPath knIpAddr = createString("knIpAddr");

    public final StringPath knIsEmailAuth = createString("knIsEmailAuth");

    public final StringPath knIsLoginAuth = createString("knIsLoginAuth");

    public final DateTimePath<java.time.LocalDateTime> knLastLoginDate = createDateTime("knLastLoginDate", java.time.LocalDateTime.class);

    public final StringPath knName = createString("knName");

    public final StringPath knOtpKey = createString("knOtpKey");

    public final StringPath knPassword = createString("knPassword");

    public final StringPath knPhoneNumber = createString("knPhoneNumber");

    public final StringPath knPwdAuthNumber = createString("knPwdAuthNumber");

    public final DateTimePath<java.time.LocalDateTime> knPwdChangeDate = createDateTime("knPwdChangeDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> knPwdErrorCount = createNumber("knPwdErrorCount", Integer.class);

    public final StringPath knReason = createString("knReason");

    public final NumberPath<Integer> knRegType = createNumber("knRegType", Integer.class);

    public final EnumPath<com.app.kokonut.admin.enums.AuthorityRole> knRoleCode = createEnum("knRoleCode", com.app.kokonut.admin.enums.AuthorityRole.class);

    public final NumberPath<Integer> knState = createNumber("knState", Integer.class);

    public final NumberPath<Integer> knUserType = createNumber("knUserType", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> knWithdrawalDate = createDateTime("knWithdrawalDate", java.time.LocalDateTime.class);

    public final StringPath knWithdrawalReason = createString("knWithdrawalReason");

    public final NumberPath<Integer> knWithdrawalReasonType = createNumber("knWithdrawalReasonType", Integer.class);

    public final NumberPath<Long> masterId = createNumber("masterId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> modify_date = createDateTime("modify_date", java.time.LocalDateTime.class);

    public final StringPath modify_email = createString("modify_email");

    public final NumberPath<Long> modify_id = createNumber("modify_id", Long.class);

    public QAdmin(String variable) {
        super(Admin.class, forVariable(variable));
    }

    public QAdmin(Path<? extends Admin> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAdmin(PathMetadata metadata) {
        super(Admin.class, metadata);
    }

}

