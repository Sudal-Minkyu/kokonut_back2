package com.app.kokonutremove;

import com.app.kokonutuser.dtos.KokonutRemoveInfoDto;

import java.util.List;
import java.util.Map;

public interface DynamicRemoveRepositoryCustom {

    void userCommonTable(String commonQuery); // 생성, 삭제, 업데이트 실행 공용

    int selectExistTable(String companyCode); // 보내는 값 - tableName = companyCode

    List<Map<String, Object>> selectUserList(String searchQuery); // kokonut_user 회원리스트 조회

    int selectUserListCount(String companyCode); // kokonut_user 회원수 조회

    List<KokonutRemoveInfoDto> selectUserDataByIdx(String searchQuery); // kokonut_user 단일회원 조회

}
