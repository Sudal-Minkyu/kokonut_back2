package com.app.kokonut.policy.policyoutdetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark :
 */
@Repository
public interface PolicyOutDetailRepository extends JpaRepository<PolicyOutDetail, Long>, JpaSpecificationExecutor<PolicyOutDetail>, PolicyOutDetailRepositoryCustom {
    @Transactional
    @Modifying
    @Query("delete from PolicyOutDetail a where a.piId = :piId")
    void deleteField(Long piId);
}