package com.app.kokonut.policy.policyresponsible;

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
public interface PolicyResponsibleRepository extends JpaRepository<PolicyResponsible, Long>, JpaSpecificationExecutor<PolicyResponsible>, PolicyResponsibleRepositoryCustom {

}