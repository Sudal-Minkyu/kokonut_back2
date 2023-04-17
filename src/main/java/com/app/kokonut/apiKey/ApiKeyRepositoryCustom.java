package com.app.kokonut.apiKey;

import com.app.kokonut.apiKey.dtos.ApiKeyDto;
import com.app.kokonut.apiKey.dtos.ApiKeyInfoDto;

/**
 * @author Woody
 * Date : 2022-10-25
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 ApiKey Sql 쿼리호출
 */
public interface ApiKeyRepositoryCustom {

//    // ApiKey 리스트 조회
//    List<ApiKeyListAndDetailDto> findByApiKeyList(ApiKeyMapperDto apiKeyMapperDto); // SelectApiKeyList -> 변경후
//
//    // ApiKey 리스트의 Count 조회
//    Long findByApiKeyListCount(ApiKeyMapperDto apiKeyMapperDto); // SelectApiKeyListCount -> 변경후
//
//    // ApiKey 단일 조회(상세보기) : param -> idx
//    ApiKeyListAndDetailDto findByApiKeyDetail(Integer idx); // SelectApiKeyByIdx -> 변경후
//
//    // ApiKey 단일 조회 : param -> key
//    ApiKeyKeyDto findByKey(String key); // SelectByKey -> 변경후
//
//    // TestApiKey 단일 조회 : param -> companyId, type = 2
//    ApiKeyListAndDetailDto findByTestApiKeyBycompanyId(Long companyId, Integer type); // SelectTestApiKeyBycompanyId -> 변경후
//
//    // TestApiKey 중복 조회 : param -> key, type = 2
//    Long findByTestApiKeyDuplicateCount(String key, Integer type); // SelectTestApiKeyDuplicateCount -> 변경후
//
//    // ApiKey 단일 조회 : param -> companyId, type = 1, useYn = "Y"
//    ApiKeyListAndDetailDto findByApiKeyBycompanyId(Long companyId, Integer type, String useYn); // SelectApiKeyBycompanyId -> 변경후
//
//    // ApiKey 중복 조회 : param -> key, type = 1
//    Long findByApiKeyDuplicateCount(String key, Integer type); // SelectApiKeyDuplicateCount -> 변경후
//
//    // TestApiKey 만료예정 리스트 조회
//    List<TestApiKeyExpiredListDto> findByTestApiKeyExpiredList(HashMap<String, Object> paramMap, Integer type); // SelectTestApiKeyExpiredList -> 변경후

    ApiKeyDto findByApiKey(Long adminId, Long companyId); // API Key 존재여부

    Long findByCheck(String akKey); // ApiKey가 존재하는지 그리고 유효한지 검증하는 메서드

    ApiKeyInfoDto findByApiKeyInfo(String apikey);

    Long findByApiKeyCheck(String userIp);

}