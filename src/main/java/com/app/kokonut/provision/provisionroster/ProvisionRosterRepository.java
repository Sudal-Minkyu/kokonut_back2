package com.app.kokonut.provision.provisionroster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Woody
 * Date : 2023-05-12
 * Time :
 * Remark :
 */
@Repository
public interface ProvisionRosterRepository extends JpaRepository<ProvisionRoster, Long>, JpaSpecificationExecutor<ProvisionRoster> {

    boolean existsByProCodeAndAdminId(String proCode, Long adminId);

}
