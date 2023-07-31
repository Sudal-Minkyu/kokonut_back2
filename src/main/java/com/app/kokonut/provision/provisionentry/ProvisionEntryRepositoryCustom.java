package com.app.kokonut.provision.provisionentry;

import com.app.kokonut.provision.provisionentry.dtos.ProvisionEntryTargetsDto;

/**
 * @author Woody
 * Date : 2023-05-11
 * Time :
 * Remark : ProvisionDownloadHistory Sql 쿼리호출
 */
public interface ProvisionEntryRepositoryCustom {

    ProvisionEntryTargetsDto findByProvisionEntryTargets(String proCode, String pipeTableName);

}