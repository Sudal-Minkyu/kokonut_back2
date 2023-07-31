package com.app.kokonut.alimtalk.alimtalkhistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AlimTalkHistoryRepository extends JpaRepository<AlimTalkHistory, Long>, JpaSpecificationExecutor<AlimTalkHistory> {

}