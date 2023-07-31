package com.app.kokonut.provision.provisionlist;

import com.app.kokonut.provision.provisionlist.dtos.ProvisionTargetIdxDto;

/**
 * @author Woody
 * Date : 2023-07-31
 * Time :
 * Remark : ProvisionList Sql 쿼리호출
 */
public interface ProvisionListRepositoryCustom {

    ProvisionTargetIdxDto findByProvisionIdxList(String proCode);

}