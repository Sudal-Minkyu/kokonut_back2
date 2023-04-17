package com.app.kokonutapi.personalInfoProvision.personalInfoDownloadHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-01-17
 * Remark :
 */
@Repository
public interface PersonalInfoDownloadHistoryRepository extends JpaRepository<PersonalInfoDownloadHistory, Long>, JpaSpecificationExecutor<PersonalInfoDownloadHistory> {

}