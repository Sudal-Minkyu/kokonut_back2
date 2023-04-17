package com.app.kokonut.qna;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QnaRepository extends JpaRepository<Qna, Long>, JpaSpecificationExecutor<Qna>, QnaRepositoryCustom {

}