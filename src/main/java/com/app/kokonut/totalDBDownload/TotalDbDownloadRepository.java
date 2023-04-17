package com.app.kokonut.totalDBDownload;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-01-13
 * Time :
 * Remark : TotalDbDownload Repository
 */
@Repository
public interface TotalDbDownloadRepository extends JpaRepository<TotalDbDownload, Long>, JpaSpecificationExecutor<TotalDbDownload>, TotalDbDownloadRepositoryCustom {

}