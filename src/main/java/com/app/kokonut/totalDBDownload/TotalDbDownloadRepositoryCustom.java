package com.app.kokonut.totalDBDownload;

import com.app.kokonut.totalDBDownload.dtos.TotalDbDownloadListDto;
import com.app.kokonut.totalDBDownload.dtos.TotalDbDownloadSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Woody
 * Date : 2023-01-13
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 TotalDbDownload Sql 쿼리호출
 */
public interface TotalDbDownloadRepositoryCustom {

    Page<TotalDbDownloadListDto> findByTotalDbDownloadList(TotalDbDownloadSearchDto totalDbDownloadSearchDto, String businessNumber, Pageable pageable);

}
