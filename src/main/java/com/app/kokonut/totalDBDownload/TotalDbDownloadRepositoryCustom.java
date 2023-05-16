package com.app.kokonut.totalDBDownload;

import com.app.kokonut.totalDBDownload.dtos.TotalDbDownloadListDto;
import com.app.kokonut.totalDBDownload.dtos.TotalDbDownloadSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Woody
 * Date : 2023-01-13
 * Time :
 * Remark : TotalDbDownload Sql 쿼리호출
 */
public interface TotalDbDownloadRepositoryCustom {

    Page<TotalDbDownloadListDto> findByTotalDbDownloadList(TotalDbDownloadSearchDto totalDbDownloadSearchDto, String businessNumber, Pageable pageable);

}
