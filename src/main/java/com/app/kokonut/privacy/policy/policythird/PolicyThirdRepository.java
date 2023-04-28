package com.app.kokonut.privacy.policy.policythird;

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
public interface PolicyThirdRepository extends JpaRepository<PolicyThird, Long>, JpaSpecificationExecutor<PolicyThird>, PolicyThirdRepositoryCustom {

}