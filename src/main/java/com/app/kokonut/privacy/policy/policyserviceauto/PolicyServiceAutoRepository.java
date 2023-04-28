package com.app.kokonut.privacy.policy.policyserviceauto;

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
public interface PolicyServiceAutoRepository extends JpaRepository<PolicyServiceAuto, Long>, JpaSpecificationExecutor<PolicyServiceAuto>, PolicyServiceAutoRepositoryCustom {

}