package com.app.kokonut.privacy.policy.policyoutdetail;

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
public interface PolicyOutDetailRepository extends JpaRepository<PolicyOutDetail, Long>, JpaSpecificationExecutor<PolicyOutDetail>, PolicyOutDetailRepositoryCustom {

}