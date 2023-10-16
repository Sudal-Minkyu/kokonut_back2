package com.app.kokonut.policy.policystatute;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Woody
 * Date : 2023-10-16
 * Time :
 * Remark :
 */
@Repository
public interface PolicyStatuteRepository extends JpaRepository<PolicyStatute, Long>, JpaSpecificationExecutor<PolicyStatute>, PolicyStatuteRepositoryCustom {

    @Transactional
    @Modifying
    @Query("delete from PolicyStatute a where a.piId = :piId")
    void deleteField(Long piId);

}