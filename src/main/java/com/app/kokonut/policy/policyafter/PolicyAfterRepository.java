package com.app.kokonut.policy.policyafter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark :
 */
@Repository
public interface PolicyAfterRepository extends JpaRepository<PolicyAfter, Long>, JpaSpecificationExecutor<PolicyAfter>, PolicyAfterRepositoryCustom {

}