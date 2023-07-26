package com.app.kokonut.thirdparty;

import com.app.kokonut.thirdparty.dtos.ThirdPartyAlimTalkSettingDto;

/**
 * @author Woody
 * Date : 2023-07-26
 * Time :
 * Remark : ThirdParty Sql 쿼리호출
 */
public interface ThirdPartyRepositoryCustom {

    ThirdPartyAlimTalkSettingDto findByAlimTalkSetting(String cpCode);

}