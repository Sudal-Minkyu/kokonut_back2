package com.app.kokonut.apikey;

import com.app.kokonut.apikey.dtos.ApiKeyDto;
import com.app.kokonut.apikey.dtos.ApiKeyInfoDto;

/**
 * @author Woody
 * Date : 2022-10-25
 * Time :
 * Remark : ApiKey Sql 쿼리호출
 */
public interface ApiKeyRepositoryCustom {

    ApiKeyDto findByApiKey(Long adminId, Long companyId); // API Key 존재여부

    Long findByCheck(String akKey); // ApiKey가 존재하는지 그리고 유효한지 검증하는 메서드

    ApiKeyInfoDto findByApiKeyInfo(String apikey, String ip);

    Long findByApiKeyCheck(String userIp);

    boolean doesAccessIpExist(Long companyId, String accessIp);

}