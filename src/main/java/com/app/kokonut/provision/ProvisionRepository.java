package com.app.kokonut.provision;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-05-10
 * Time :
 * Remark :
 */
@Repository
public interface ProvisionRepository extends JpaRepository<Provision, Long>, JpaSpecificationExecutor<Provision>, ProvisionRepositoryCustom {

}