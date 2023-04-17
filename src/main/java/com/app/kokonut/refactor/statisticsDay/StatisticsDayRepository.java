package com.app.kokonut.refactor.statisticsDay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StatisticsDayRepository extends JpaRepository<StatisticsDay, Long>, JpaSpecificationExecutor<StatisticsDay> {

}