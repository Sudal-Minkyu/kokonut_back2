package com.app.kokonut.revisedDocumentFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RevisedDocumentFileRepository extends JpaRepository<RevisedDocumentFile, Long> , JpaSpecificationExecutor<RevisedDocumentFile> {
}