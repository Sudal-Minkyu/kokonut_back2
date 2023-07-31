package com.app.kokonut.provision.provisionentry;

import com.app.kokonut.provision.provisionentry.dtos.ProvisionEntryTargetsDto;

/**
 * @author Woody
 * Date : 2023-07-31
 * Time :
 * Remark : ProvisionEntry Sql 쿼리호출
 */
public interface ProvisionEntryRepositoryCustom {

    ProvisionEntryTargetsDto findByProvisionEntryTargets(String proCode, String pipeTableName);

}