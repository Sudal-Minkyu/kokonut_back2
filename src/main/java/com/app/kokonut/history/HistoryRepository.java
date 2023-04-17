package com.app.kokonut.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long>, JpaSpecificationExecutor<History>, HistoryRepositoryCustom {

//    public void insertHistory(HashMap<String, Object> paramMap);
//
//    public void updateHistory(HashMap<String, Object> paramMap);
//
//    public void DeleteActivityHistoryByIdx(int idx);
//
//    public void updateHistoryReasonByIdx(HashMap<String, Object> paramMap);


//    public void DeleteActivityHistoryBycompanyId(Long companyId);
//
//    public void DeleteExpiredActivityHistory(Map<String, Object> map);

}