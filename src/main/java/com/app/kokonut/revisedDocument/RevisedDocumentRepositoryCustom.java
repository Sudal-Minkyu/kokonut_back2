package com.app.kokonut.revisedDocument;

import com.app.kokonut.revisedDocument.dtos.RevDocListDto;
import com.app.kokonut.revisedDocument.dtos.RevDocSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Joy
 * Date : 2023-01-04
 * Time :
 * Remark : RevisedDocumentRepository 개인정보 처리방침 - 개정문서 기존 쿼리 호출
 */
public interface RevisedDocumentRepositoryCustom {
    // 개정문서 목록 조회 - 기존 SelectRevisedDocumentList, SelectRevisedDocumentListCount
    Page<RevDocListDto> findRevDocPage(Long companyId, RevDocSearchDto revDocSearchDto, Pageable pageable);

    // 개정문서 상세 조회 - 기존 SelectRevisedDocumentByIdx
    // 개정문서 등록 - InsertRevisedDocument
    // 개정문서 삭제 - 기존 DeleteRevisedDocumentByIdx
    // 개정문서 전체 삭제 - 기존 DeleteBycompanyId
}