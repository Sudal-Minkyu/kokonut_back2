package com.app.kokonut.apiKey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

/**
 * @author Woody
 * Date : 2022-10-25
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 ApiKeyDao라고 보면 됨
 */
@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long>, JpaSpecificationExecutor<ApiKey>, ApiKeyRepositoryCustom {

    Optional<ApiKey> findApiKeyByAdminIdAndCompanyId(Long adminId, Long companyId);

    boolean existsByAkKey(String akKey); // ApiKey가 존재하는지 체크

    boolean existsByAdminIdAndCompanyId(Long adminId, Long companyId); // 사용안함 2023/03/23 woody

//    Optional<ApiKey> findApiKeyBycompanyIdAndType(Long companyId, Integer type);
//
//    @Query("select a from ApiKey a where a.companyId = :companyId and a.type = :type and (a.validityStart <= :validityStart and :validityStart < a.validityEnd)")
//    Optional<ApiKey> findApiKeyBycompanyIdAndTypeDate(@Param("companyId") Long companyId, @Param("type") Integer type, @Param("validityStart") Date validityStart);
//
//    Optional<ApiKey> findApiKeyBycompanyId(Long companyId);

//     List<HashMap<String, Object>> SelectApiKeyList(HashMap<String, Object> paramMap); // 변경전 - RepositoryCustom 완료 @@@@
//
//     int SelectApiKeyListCount(HashMap<String, Object> paramMap); // 변경전 - RepositoryCustom 완료 @@@@
//
//     HashMap<String, Object> SelectByKey(String key); // 변경전 - RepositoryCustom 완료 @@@@
//
//     HashMap<String, Object> SelectApiKeyByIdx(int idx); // 변경전 - RepositoryCustom 완료 @@@@
//
//     void InsertApiKey(HashMap<String, Object> paramMap); // 변경전 - Service 완료 @@@@
//
//     void UpdateApiKey(HashMap<String, Object> paramMap); // 변경전 - Service 완료 @@@@
//
//     void DeleteApiKeyByIdx(int idx); // 변경전 - Service 완료 @@@@
//
//     HashMap<String, Object> SelectTestApiKeyBycompanyId(HashMap<String, Object> paramMap); // 변경전 - RepositoryCustom 완료 @@@@
//
//     int SelectTestApiKeyDuplicateCount(String key); // 변경전 - RepositoryCustom 완료 @@@@
//
//     HashMap<String, Object> SelectApiKeyBycompanyId(HashMap<String, Object> paramMap); // 변경전 - RepositoryCustom 완료 @@@@
//
//     int SelectApiKeyDuplicateCount(String key); // 변경전 - RepositoryCustom 완료 @@@@
//
//     List<HashMap<String, Object>> SelectTestApiKeyExpiredList(HashMap<String, Object> paramMap); // 변경전 - RepositoryCustom 완료 @@@@
//
//     void UpdateBlockKey(Long companyId); // 변경전 - Service 완료 @@@@
//
//     void UpdateTestKeyExpire(Long companyId); // 변경전 - Service 완료 @@@@
//
//     void DeleteApiKeyBycompanyId(Long companyId); // 변경전 - Service 완료 @@@@

}