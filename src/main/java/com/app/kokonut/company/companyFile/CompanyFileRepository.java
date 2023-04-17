package com.app.kokonut.company.companyFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2022-12-22
 * Time :
 * Remark : 기업 파일업로드 Repository
 */
@Repository
public interface CompanyFileRepository extends JpaRepository<CompanyFile, Long>, JpaSpecificationExecutor<CompanyFile> {

}