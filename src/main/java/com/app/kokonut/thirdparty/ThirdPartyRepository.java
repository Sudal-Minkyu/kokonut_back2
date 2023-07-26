package com.app.kokonut.thirdparty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Woody
 * Date : 2023-07-24
 * Time :
 * Remark :
 */
@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Long>, JpaSpecificationExecutor<ThirdParty>, ThirdPartyRepositoryCustom {

    Optional<ThirdParty> findThirdPartyByCpCodeAndTsType(String cpCode, String tsType);

}