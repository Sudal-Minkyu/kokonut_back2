package com.app.kokonut.provision.provisiondownloadhistory;

import com.app.kokonut.provision.provisiondownloadhistory.dtos.ProvisionDownloadHistoryListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Woody
 * Date : 2023-05-11
 * Time :
 * Remark : ProvisionDownloadHistory Sql 쿼리호출
 */
public interface ProvisionDownloadHistoryRepositoryCustom {

    Page<ProvisionDownloadHistoryListDto> findByProvisionDownloadList(String proCode, Pageable pageable);

}