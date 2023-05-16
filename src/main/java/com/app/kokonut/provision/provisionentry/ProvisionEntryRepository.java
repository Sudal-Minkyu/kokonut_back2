package com.app.kokonut.provision.provisionentry;

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
public interface ProvisionEntryRepository extends JpaRepository<ProvisionEntry, Long>, JpaSpecificationExecutor<ProvisionEntry> {

}