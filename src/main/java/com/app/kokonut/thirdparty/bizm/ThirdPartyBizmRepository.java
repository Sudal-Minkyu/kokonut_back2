package com.app.kokonut.thirdparty.bizm;

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
public interface ThirdPartyBizmRepository extends JpaRepository<ThirdPartyBizm, Long>, JpaSpecificationExecutor<ThirdPartyBizm> {

    Optional<ThirdPartyBizm> findThirdPartyBizmByTsId(Long tsId);

}