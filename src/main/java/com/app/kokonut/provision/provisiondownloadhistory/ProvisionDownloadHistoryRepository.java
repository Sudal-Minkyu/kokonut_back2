package com.app.kokonut.provision.provisiondownloadhistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Woody
 * Date : 2023-05-11
 * Time :
 * Remark :
 */
@Repository
public interface ProvisionDownloadHistoryRepository extends JpaRepository<ProvisionDownloadHistory, Long>, JpaSpecificationExecutor<ProvisionDownloadHistory>, ProvisionDownloadHistoryRepositoryCustom {

    Optional<ProvisionDownloadHistory> findProvisionDownloadHistoryByProCodeAndAdminId(String proCode, Long adminId);

}