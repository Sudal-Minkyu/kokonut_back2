package com.app.kokonut.qnaFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Joy
 * Date : 2022-12-28
 * Time :
 * Remark : 1:1 문의 파일업로드 Repository
 */
@Repository
public interface QnaFileRepository extends JpaRepository<QnaFile, Long>, JpaSpecificationExecutor<QnaFile>, QnaFileRepositoryCustom {

}