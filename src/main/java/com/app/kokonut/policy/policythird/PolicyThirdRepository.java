package com.app.kokonut.policy.policythird;

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
public interface PolicyThirdRepository extends JpaRepository<PolicyThird, Long>, JpaSpecificationExecutor<PolicyThird>, PolicyThirdRepositoryCustom {
    @Transactional
    @Modifying
    @Query("delete from PolicyThird a where a.piId = :piId")
    void deleteField(Long piId);
}