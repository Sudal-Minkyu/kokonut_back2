package com.app.kokonut.policy.policyafter;

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
public interface PolicyAfterRepository extends JpaRepository<PolicyAfter, Long>, JpaSpecificationExecutor<PolicyAfter>, PolicyAfterRepositoryCustom {
    @Transactional
    @Modifying
    @Query("delete from PolicyAfter a where a.piId = :piId")
    void deleteField(Long piId);
}