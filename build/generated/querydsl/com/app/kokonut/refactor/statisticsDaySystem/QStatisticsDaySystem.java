package com.app.kokonut.refactor.statisticsDaySystem;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStatisticsDaySystem is a Querydsl query type for StatisticsDaySystem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStatisticsDaySystem extends EntityPathBase<StatisticsDaySystem> {

    private static final long serialVersionUID = 1734190297L;

    public static final QStatisticsDaySystem statisticsDaySystem = new QStatisticsDaySystem("statisticsDaySystem");

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final NumberPath<Integer> sbsAdminHistory = createNumber("sbsAdminHistory", Integer.class);

    public final NumberPath<Integer> sbsAutoCancel = createNumber("sbsAutoCancel", Integer.class);

    public final NumberPath<Integer> sbsBasic = createNumber("sbsBasic", Integer.class);

    public final NumberPath<Integer> sbsBasicAmount = createNumber("sbsBasicAmount", Integer.class);

    public final NumberPath<Integer> sbsDormant = createNumber("sbsDormant", Integer.class);

    public final NumberPath<Integer> sbsNewAdminMember = createNumber("sbsNewAdminMember", Integer.class);

    public final NumberPath<Integer> sbsNewMasterMember = createNumber("sbsNewMasterMember", Integer.class);

    public final NumberPath<Integer> sbsPersonalHistory = createNumber("sbsPersonalHistory", Integer.class);

    public final NumberPath<Integer> sbsPremium = createNumber("sbsPremium", Integer.class);

    public final NumberPath<Integer> sbsPremiumAmount = createNumber("sbsPremiumAmount", Integer.class);

    public final NumberPath<Integer> sbsStandard = createNumber("sbsStandard", Integer.class);

    public final NumberPath<Integer> sbsStandardAmount = createNumber("sbsStandardAmount", Integer.class);

    public final NumberPath<Integer> sbsStandardUser = createNumber("sbsStandardUser", Integer.class);

    public final NumberPath<Integer> sbsWithdrawal = createNumber("sbsWithdrawal", Integer.class);

    public final NumberPath<Integer> sbsWithdrawalCancel = createNumber("sbsWithdrawalCancel", Integer.class);

    public final NumberPath<Integer> sds_new_member = createNumber("sds_new_member", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> sdsDate = createDateTime("sdsDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> sdsId = createNumber("sdsId", Long.class);

    public QStatisticsDaySystem(String variable) {
        super(StatisticsDaySystem.class, forVariable(variable));
    }

    public QStatisticsDaySystem(Path<? extends StatisticsDaySystem> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStatisticsDaySystem(PathMetadata metadata) {
        super(StatisticsDaySystem.class, metadata);
    }

}

