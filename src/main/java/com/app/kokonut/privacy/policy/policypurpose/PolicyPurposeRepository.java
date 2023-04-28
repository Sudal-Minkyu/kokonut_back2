package com.app.kokonut.privacy.policy.policypurpose;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-04-25
 * Time :
 * Remark :
 */
@Repository
public interface PolicyPurposeRepository extends JpaRepository<PolicyPurpose, Long>, JpaSpecificationExecutor<PolicyPurpose>, PolicyPurposeRepositoryCustom {

}