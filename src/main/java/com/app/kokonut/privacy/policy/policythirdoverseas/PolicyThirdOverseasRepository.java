package com.app.kokonut.privacy.policy.policythirdoverseas;

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
public interface PolicyThirdOverseasRepository extends JpaRepository<PolicyThirdOverseas, Long>, JpaSpecificationExecutor<PolicyThirdOverseas>, PolicyThirdOverseasRepositoryCustom {

}