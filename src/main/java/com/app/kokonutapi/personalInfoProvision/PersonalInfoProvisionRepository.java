package com.app.kokonutapi.personalInfoProvision;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2022-11-01
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 PersonalInfoDao라고 보면 됨
 */
@Repository
public interface PersonalInfoProvisionRepository extends JpaRepository<PersonalInfoProvision, Long>, JpaSpecificationExecutor<PersonalInfoProvision>, PersonalInfoProvisionRepositoryCustom {

    /////////////////////////////////////////////////////////////////
    // 정보제공
    /////////////////////////////////////////////////////////////////
//    String selectProvisionLatestNumber(String prefix); // 변경전 - RepositoryCustom 완료 @@@@
//    Map<String, Object> selectProvision(String number); // 변경전 - RepositoryCustom 완료 @@@@

//    Optional<PersonalInfoProvision> findPersonalInfoProvisionBycompanyIdAndNumber(Long companyId, String number); // selectProvisionByNumberAndcompanyId -> 변경후
//    Optional<PersonalInfoProvision> findPersonalInfoProvisionByRecipientEmailAndNumber(String number, String recipientEmail); // selectProvisionByNumberAndRecipientEmail -> 변경후

//    List<HashMap<String, Object>> selectProvisionList(HashMap<String, Object> map); // 변경전 - RepositoryCustom 완료 @@@@
//    int selectProvisionListCount(Map<String, Object> map); // 변경전 - RepositoryCustom 완료 @@@@ -> selectProvisionList의 size() 해도 되지않을까?... 일단 이건 이렇게 조치

//    Optional<PersonalInfoProvision> findProvisionInfoProvisionByStartDate(Date startDate); // selectProvisionListByStartDate -> 변경후
//    Optional<PersonalInfoProvision> findProvisionInfoProvisionByRecipientEmail(String recipientEmail); // selectProvisionListByRecipientEmail -> 변경후

//    int insertProvision(PersonalInfoProvisionDto personalInfoProvisionDto);
//    int updateProvision(PersonalInfoProvisionDto personalInfoProvisionDto);
//    int updateProvisionColumns(Map<String, Object> map);
//    int updateProvisionTargets(Map<String, Object> map);
//    int deleteProvision(String number);

//    /////////////////////////////////////////////////////////////////
//    // 정보제공 수정 이력
//    /////////////////////////////////////////////////////////////////
//    Map<String, Object> selectLatestProvisionHistory(String number);
//    int insertProvisionHistory(Map<String, Object> map);
//    int deleteProvisionHistory(String number);
//
//    /////////////////////////////////////////////////////////////////
//    // 정보제공 별도 수집
//    /////////////////////////////////////////////////////////////////
//    List<HashMap<String, Object>> selectProvisionAgreeList(HashMap<String, Object> map);
//    int selectProvisionAgreeListCount(Map<String, Object> map);
//    Map<String, Object> selectProvisionAgree(Map<String, Object> map);
//    List<Map<String, Object>> selectProvisionAgreeListByNumber(String number);
//    int insertTempProvisionAgree(Map<String, Object> map);
//    int insertProvisionAgree(PersonalInfoProvisionAgreeData data);
//    int updateProvisionAgree(Map<String, Object> map);
//    int deleteProvisionAgree(Map<String, Object> map);
//    int deleteProvisionAgreeByNumber(String number);
//    int deleteProvisionAgreeByIdx(int idx);
//
//    /////////////////////////////////////////////////////////////////
//    // 정보제공 다운로드 이력
//    /////////////////////////////////////////////////////////////////
//    List<Map<String, Object>> selectDownloadHistoryList(String number);
//    void updateDownloadHistory(Map<String, Object> map);
//    List<Map<String, Object>> selectDownloadHistoryListByIdx(String idx);
//    int deleteDownloadHistoryByNumber(String number);
//
}