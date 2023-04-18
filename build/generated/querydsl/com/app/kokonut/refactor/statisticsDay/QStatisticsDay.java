package com.app.kokonut.refactor.statisticsDay;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStatisticsDay is a Querydsl query type for StatisticsDay
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStatisticsDay extends EntityPathBase<StatisticsDay> {

    private static final long serialVersionUID = -1188488709L;

    public static final QStatisticsDay statisticsDay = new QStatisticsDay("statisticsDay");

    public final NumberPath<Long> companyId = createNumber("companyId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> insert_date = createDateTime("insert_date", java.time.LocalDateTime.class);

    public final NumberPath<Integer> sdAdminHistory = createNumber("sdAdminHistory", Integer.class);

    public final NumberPath<Integer> sdAllMember = createNumber("sdAllMember", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> sdDate = createDateTime("sdDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> sdDormant = createNumber("sdDormant", Integer.class);

    public final NumberPath<Long> sdId = createNumber("sdId", Long.class);

    public final NumberPath<Integer> sdNewMember = createNumber("sdNewMember", Integer.class);

    public final NumberPath<Integer> sdPersonalHistory = createNumber("sdPersonalHistory", Integer.class);

    public final NumberPath<Integer> sdWithdrawal = createNumber("sdWithdrawal", Integer.class);

    public QStatisticsDay(String variable) {
        super(StatisticsDay.class, forVariable(variable));
    }

    public QStatisticsDay(Path<? extends StatisticsDay> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStatisticsDay(PathMetadata metadata) {
        super(StatisticsDay.class, metadata);
    }

}

