package com.app.kokonut.policy.policybefore;

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
public interface PolicyBeforeRepository extends JpaRepository<PolicyBefore, Long>, JpaSpecificationExecutor<PolicyBefore>, PolicyBeforeRepositoryCustom {
    @Transactional
    @Modifying
    @Query("delete from PolicyBefore a where a.piId = :piId")
    void deleteField(Long piId);
}