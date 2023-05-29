package com.app.kokonutuser;

import com.app.kokonutuser.dtos.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DynamicUserRepositoryCustom {

    List<KokonutUserFieldDto> selectColumns(String searchQuery); // 테이블의 컬럼 조회

    List<Map<String, Object>> getCommentOrEncrypt(String searchQuery); // 테이블의 필드 조회

    int verificationQuery(String queryStr); // 검증쿼리문 전용

    String getTableDataCheck(String queryStr); // 테이블이 하나라도 존재하는지 여부를 체크

    List<Map<String, Object>> privacyListPagedData(String queryStr); // 개인정보 리스트호출

    int privacyListTotal(String queryStr); // 개인정보 리스트의 총합계

    void userCommonTable(String commonQuery); // 생성, 삭제, 업데이트 실행 공용

    int selectExistUserTable(String companyCode); // 보내는 값 - tableName = companyCode

    List<Map<String, Object>> selectUserList(String searchQuery); // kokonut_user 회원리스트 조회

    int selectUserListCount(String companyCode); // kokonut_user 회원수 조회

    List<KokonutRemoveInfoDto> selectUserDataByIdx(String searchQuery); // kokonut_user 단일회원 조회

    List<KokonutOneYearAgeRegUserListDto> selectOneYearAgoRegUserListByTableName(String searchQuery); // 1년전에 가입한 회원목록 조회

    Integer selectUserCount(String searchQuery); // 회원 등록여부 조회

    Integer selectTableLastIdx(String searchQuery); // 저장 유저의 마지막 IDX 조회

    String selectIdByFieldAndValue(String searchQuery); // 필드값을 통해 아이디 조회

    List<KokonutUserFieldInfoDto> selectUserFieldList(String VALUE, String searchQuery); // 필드-값 쌍으로 사용자 컬럼값 조회

    List<KokonutUserPwInfoDto> findByNowPw(String searchQuery); // 현재 비밀번호 값 호출

    Integer selectCountByThisMonth(String searchQuery); // 금일부터 한달전까지 속해 있는 유저수 조회

    List<KokonutUserListDto> findByUserPage(String searchQuery); // 유저 리스트 조회

    Integer selectUserIdCheck(String searchQuery); // 아이디 존재 유무 확인

    Long selectUserIdx(String searchQuery); // 유저 ID를 통해 IDX를 조회

    String selectUserColumnComment(String searchQuery); // 유저테이블의 필드명을 통해 Comment 조회

    List<KokonutUserFieldCheckDto> selectUserTableNameAndFieldName(String searchQuery); // 개인정보 테이블의 필드명을 통해 테이블명, 필드명 조회 -> 삭제하기위해 조회하는 메서드
}
