package com.app.kokonut.service;
import com.app.kokonut.service.dtos.KnServiceDto;
import java.util.List;

/**
 * @author Joy
 * Date : 2023-01-05
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 ServiceDao 쿼리호출
 */
public interface KnServiceRepositoryCustom {

    // ServiceDao
    // 서비스(구독권) 목록 조회 - 기존 SelectServiceList
    List<KnServiceDto> findServiceList();

    // 서비스(구독권) 상세 보기 - 기존 SelectServiceByIdx
    KnServiceDto findServiceByIdx(Long srId);

    // 서비스(구독권) 등록 하기 - 기존 InsertService
    // 서비스(구독권) 수정 하기 - 기존 UpdateService
    // 서비스(구독권) 삭제 하기 - 기존 DeleteServiceByIdx
}