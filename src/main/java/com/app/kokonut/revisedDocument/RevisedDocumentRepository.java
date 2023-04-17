package com.app.kokonut.revisedDocument;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Joy
 * Date : 2023-01-04
 * Time :
 * Remark : RevisedDocumentRepository 개인정보 처리방침 - 개정문서
 */
@Repository
public interface RevisedDocumentRepository extends JpaRepository<RevisedDocument, Long>, JpaSpecificationExecutor<RevisedDocument>, RevisedDocumentRepositoryCustom {

}