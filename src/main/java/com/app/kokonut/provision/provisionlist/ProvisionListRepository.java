package com.app.kokonut.provision.provisionlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-05-12
 * Time :
 * Remark :
 */
@Repository
public interface ProvisionListRepository extends JpaRepository<ProvisionList, Long>, JpaSpecificationExecutor<ProvisionList>, ProvisionListRepositoryCustom {

}