package com.app.kokonut.collectInformation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Joy
 * Date : 2023-01-04
 * Time :
 * Remark : CollectInformationRepository 개인정보 처리방침 - 개인정보 수집 및 이용 안내
 */
@Repository
public interface CollectInformationRepository extends JpaRepository<CollectInformation, Long>, JpaSpecificationExecutor<CollectInformation>, CollectInformationRepositoryCustom {

}