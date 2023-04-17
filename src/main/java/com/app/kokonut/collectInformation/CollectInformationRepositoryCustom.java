package com.app.kokonut.collectInformation;

import com.app.kokonut.collectInformation.dtos.CollectInfoDetailDto;
import com.app.kokonut.collectInformation.dtos.CollectInfoListDto;
import com.app.kokonut.collectInformation.dtos.CollectInfoSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Joy
 * Date : 2023-01-04
 * Time :
 * Remark : CollectInformationRepository 개인정보 처리방침 - 개인정보 수집 및 이용 안내 기존 쿼리 호출
 */
public interface CollectInformationRepositoryCustom {

    // 개인정보 처리방침 목록 조회 - 기존 SelectCollectInformationList, SelectCollectInformationListCount
    Page<CollectInfoListDto> findCollectInfoPage(Long companyId, CollectInfoSearchDto collectInfoSearchDto, Pageable pageable);

    // 개인정보 처리방침 상세 조회 - 기존 SelectCollectInformationByIdx
    CollectInfoDetailDto findCollectInfoByIdx(Long idx);

    // 개인정보 처리방침 등록 - 기존 InsertCollectInformation
    // 개인정보 처리방침 수정 - 기존 UpdateCollectInformation
    // 개인정보 처리방침 삭제 - 기존 DeleteCollectInformationByIdx
    // 개인정보 처리방침 전체 조회 - companyId를 기준으로 idx List를 조회한다. List findCollectInfoIdxByCompayId(Long companyId);
    // 개인정보 처리방침 전체 삭제 - 기존 DeleteBycompanyId (기존 TotalDeleteService에서 호출해서 처리함.)
}