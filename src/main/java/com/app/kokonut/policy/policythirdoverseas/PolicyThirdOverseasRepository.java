package com.app.kokonut.policy.policythirdoverseas;

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
public interface PolicyThirdOverseasRepository extends JpaRepository<PolicyThirdOverseas, Long>, JpaSpecificationExecutor<PolicyThirdOverseas>, PolicyThirdOverseasRepositoryCustom {
    @Transactional
    @Modifying
    @Query("delete from PolicyThirdOverseas a where a.piId = :piId")
    void deleteField(Long piId);
}