package com.app.kokonut.refactor.statisticsDaySystem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StatisticsDaySystemRepository extends JpaRepository<StatisticsDaySystem, Long>, JpaSpecificationExecutor<StatisticsDaySystem> {

}