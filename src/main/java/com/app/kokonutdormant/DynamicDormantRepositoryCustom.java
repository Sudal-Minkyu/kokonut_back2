package com.app.kokonutdormant;

import com.app.kokonutdormant.dtos.KokonutDormantFieldCheckDto;
import com.app.kokonutdormant.dtos.KokonutDormantFieldInfoDto;
import com.app.kokonutdormant.dtos.KokonutDormantListDto;
import com.app.kokonutuser.dtos.KokonutRemoveInfoDto;
import com.app.kokonutuser.dtos.KokonutUserFieldDto;

import java.util.List;
import java.util.Map;

public interface DynamicDormantRepositoryCustom {

    void dormantCommonTable(String commonQuery); // 생성, 삭제, 업데이트 실행 공용

    List<KokonutDormantListDto> findByDormantPage(String searchQuery);

    Integer selectDormantCount(String searchQuery);

    List<KokonutRemoveInfoDto> selectDormantDataByIdx(String searchQuery);

    Integer selectDormantIdCheck(String searchQuery); // 아이디 존재 유무 확인

    List<KokonutUserFieldDto> selectDormantColumns(String searchQuery); // 휴면테이블의 컬럼 조회

    List<KokonutDormantFieldInfoDto> selectDormantFieldList(String field, String searchQuery); // 필드-값 쌍으로 사용자 컬럼값 조회

    List<KokonutDormantFieldCheckDto> selectDormantTableNameAndFieldName(String searchQuery); // 휴면테이블의 필드명을 통해 테이블명, 필드명 조회 -> 삭제하기위해 조회하는 메서드

    int selectExistDormantTable(String companyCode);

    List<Map<String, Object>> selectDormantList(String searchQuery); // kokonut_dormant 회원리스트 조회
}
