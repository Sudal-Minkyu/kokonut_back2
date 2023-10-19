package com.app.kokonut.policy.policypurpose;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Woody
 * Date : 2023-04-25
 * Time :
 * Remark :
 */
@Repository
public interface PolicyPurposeRepository extends JpaRepository<PolicyPurpose, Long>, JpaSpecificationExecutor<PolicyPurpose>, PolicyPurposeRepositoryCustom {
    @Transactional
    @Modifying
    @Query("delete from PolicyServiceAuto a where a.piId = :piId")
    void deleteField(Long piId);
}