package com.app.kokonut.provision;

import com.app.kokonut.provision.dtos.ProvisionListDto;
import com.app.kokonut.provision.dtos.ProvisionSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Woody
 * Date : 2023-05-10
 * Time :
 * Remark : Provision Sql 쿼리호출
 */
public interface ProvisionRepositoryCustom {

    Page<ProvisionListDto> findByProvisionList(ProvisionSearchDto provisionSearchDto, Pageable pageable);

}