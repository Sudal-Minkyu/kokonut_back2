package com.app.kokonutuser;

import com.app.kokonutuser.dtos.*;
import com.app.kokonutuser.dtos.use.KokonutUserAlimTalkFieldDto;
import com.app.kokonutuser.dtos.use.KokonutUserEmailFieldDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Woody
 * Date : 2022-12-27
 * Time :
 * Remark : Kokonut-user DB & 테이블 - 쿼리문 선언부 모두 NativeQuery로 실행한다.
 */
@Slf4j
@Repository
public class DynamicUserRepositoryCustomImpl implements DynamicUserRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DynamicUserRepositoryCustomImpl(@Qualifier("kokonutUserJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 테이블의 컬럼 조회
    @Override
    public List<KokonutUserFieldDto> selectColumns(String searchQuery) {
        return jdbcTemplate.query(searchQuery,
                new BeanPropertyRowMapper<>(KokonutUserFieldDto.class));
    }

    // 테이블의 필드 조회
    @Override
    public List<Map<String, Object>> getCommentOrEncrypt(String searchQuery) {
        return jdbcTemplate.queryForList(searchQuery);
    }

    // 검증쿼리문 전용
    @Override
    public int verificationQuery(String queryStr) {
        List<String> tables = jdbcTemplate.queryForList(queryStr, String.class);
        return tables.isEmpty() ? 0 : 1;
    }

    // 개인정보 리스트호출
    public List<Map<String, Object>> privacyListPagedData(String queryStr) {
        log.info("개인정보 리스트호출한 쿼리문 : "+queryStr);
        return jdbcTemplate.queryForList(queryStr);
    }

    // 개인정보 리스트의 총합계
    public int privacyListTotal(String queryStr) {
        log.info("개인정보 리스트 카운팅 호출한 쿼리문 : "+queryStr);
        Integer result = jdbcTemplate.queryForObject(queryStr, Integer.class);
        if (result != null) {
            return result;
        } else {
            return 0;
        }
    }

    // 개인정보 열람호출
    public List<Map<String, Object>> privacyOpenInfoData(String queryStr) {
        log.info("개인정보 열람 쿼리문 : "+queryStr);
        return jdbcTemplate.queryForList(queryStr);
    }

    // 테이블 데이터가 하나라도 존재하는지 검증해주는 함수
    @Override
    public String getTableDataCheck(String queryStr) {
        int existsFlag = jdbcTemplate.queryForObject(queryStr, Integer.class);
        return existsFlag == 1 ? "Y" : "N";
    }


    // 유저테이블 중복 체크 메서드
    @Override
    public int selectExistUserTable(String companyCode) {
        String sql = "SELECT EXISTS (SELECT 1 FROM Information_schema.tables WHERE table_name = "+"'"+companyCode+"'"+") AS flag";
//        log.info("중복체크 sql : "+sql);
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    // 유저테이블 생성/삭제/업데이트 메서드
    @Override
    public void userCommonTable(String commonQuery) {
        jdbcTemplate.execute(commonQuery);
    }

    // 유저테이블의 회원 조회 나중에 밑에 코드로 수정하기 Dto 정해질때
//    @Override
//    public List<가져올데이터Dto> selectUserList(String searchQuery) {
//        return jdbcTemplate.query(searchQuery,
//                new BeanPropertyRowMapper<>(가져올데이터Dto.class));
//    }
    @Override
    public List<Map<String, Object>> selectUserList(String searchQuery) {
        return jdbcTemplate.queryForList(searchQuery);
    }

    // 삭제할 유저테이블 단일회원 조회
    @Override
    public List<KokonutRemoveInfoDto> selectUserDataByIdx(String searchQuery) {
        return jdbcTemplate.query(searchQuery,
                new BeanPropertyRowMapper<>(KokonutRemoveInfoDto.class));
    }

    // 1년전에 가입한 회원목록 조회
    @Override
    public List<KokonutOneYearAgeRegUserListDto> selectOneYearAgoRegUserListByTableName(String searchQuery) {
        return jdbcTemplate.query(searchQuery,
                new BeanPropertyRowMapper<>(KokonutOneYearAgeRegUserListDto.class));
    }

    // 유저 등록여부 조회
    @Override
    public Integer selectUserCount(String searchQuery) {
        return jdbcTemplate.queryForObject(searchQuery, Integer.class);
    }

    // 저장 유저의 마지막 IDX 조회
    @Override
    public Integer selectTableLastIdx(String searchQuery) {
        return jdbcTemplate.queryForObject(searchQuery, Integer.class);
    }

    // 필드값을 통해 아이디 조회
    @Override
    public String selectIdByFieldAndValue(String searchQuery) {
        return jdbcTemplate.queryForObject(searchQuery, String.class);
    }

    // 필드-값 쌍으로 사용자 컬럼값 조회
    @Override
    public List<KokonutUserFieldInfoDto> selectUserFieldList(String VALUE, String searchQuery) {
        return jdbcTemplate.query(
            searchQuery,
            (rs, rowNum) ->
                new KokonutUserFieldInfoDto(
                    rs.getLong("kokonut_IDX"),
                    rs.getObject(VALUE)
                )
        );
    }

    // 현재 비밀번호 값 호출
    @Override
    public List<KokonutUserPwInfoDto> findByNowPw(String searchQuery) {
        return jdbcTemplate.query(
                searchQuery,
                (rs, rowNum) ->
                        new KokonutUserPwInfoDto(
                                rs.getString("kokonut_IDX"),
                                rs.getString("PASSWORD_1_pw")
                        )
        );
    }

    // 유저테이블 회원 수 조회
    public int getCountFromTable(String searchQuery) {
        Integer result = jdbcTemplate.queryForObject(searchQuery, SingleColumnRowMapper.newInstance(Integer.class));
        return Optional.ofNullable(result).orElse(0);
    }


    // 금일부터 한달전까지 속해 있는 유저수 조회
    @Override
    public Integer selectCountByThisMonth(String searchQuery) {
        return jdbcTemplate.queryForObject(searchQuery, Integer.class);
    }

//    SimpleJdbcTemplate template = new SimpleJdbcTemplate(dataSource);


    public List<KokonutUserListDto> findByUserPage(String searchQuery) {
        return jdbcTemplate.query(
            searchQuery,
            (rs, rowNum) ->
                new KokonutUserListDto(
                    rs.getLong("kokonut_IDX"),
                    rs.getString("ID"),
                    rs.getTimestamp("REGDATE").toLocalDateTime(),
                    rs.getTimestamp("LAST_LOGIN_DATE").toLocalDateTime()
                )
        );
    }

    // 아이디 존재 유무 확인
    @Override
    public Integer selectUserIdCheck(String searchQuery) {
        return jdbcTemplate.queryForObject(searchQuery, Integer.class);
    }

    // 유저 ID를 통해 IDX를 조회
    @Override
    public Long selectUserIdx(String searchQuery) {
        return jdbcTemplate.queryForObject(searchQuery, Long.class);
    }

    // 유저테이블의 필드명을 통해 Comment 조회
    @Override
    public String selectUserColumnComment(String searchQuery) {
        return jdbcTemplate.queryForObject(searchQuery, String.class);
    }

    // 개인정보 테이블의 필드명을 통해 테이블명, 필드명 조회 -> 삭제하기위해 조회하는 메서드
    @Override
    public List<KokonutUserFieldCheckDto> selectUserTableNameAndFieldName(String searchQuery) {
        return jdbcTemplate.query(
                searchQuery,
                (rs, rowNum) ->
                        new KokonutUserFieldCheckDto(
                                rs.getString("TABLE_NAME"),
                                rs.getString("COLUMN_NAME")
                        )
        );
    }

    // 이메일발송할 대상 리스트 호출
    @Override
    public List<KokonutUserEmailFieldDto> emailFieldList(String emailField, String searchQuery) {
        return jdbcTemplate.query(
                searchQuery,
                (rs, rowNum) ->
                        new KokonutUserEmailFieldDto(
                                rs.getObject(emailField)
                        )
        );
    }

    // 개인정보 테이블의 컬럼 코멘트 조회
    @Override
    public List<KokonutUserCommentInfoDto> commentInfo(String searchQuery) {
        return jdbcTemplate.query(
                searchQuery,
                (rs, rowNum) ->
                        new KokonutUserCommentInfoDto(
                                rs.getString("columnName"),
                                rs.getString("columnSecurity"),
                                rs.getString("columnSubName")
                        )
        );
    }

    public String getColumnComment(String searchQuery, String tableName, String columnName) {
        return jdbcTemplate.queryForObject(searchQuery, (rs, rowNum) -> rs.getString("COLUMN_COMMENT"), tableName, columnName);
    }

    // 필드의 존재유무 호출
    public Long getFieldCheck(String ctName, String fieldName) {
        String searchQuery = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '"+ctName+"' AND "+"COLUMN_NAME = '"+fieldName+"'";
        log.info("searchQuery : "+searchQuery);
        return jdbcTemplate.queryForObject(searchQuery, Long.class);
    }

//    // 알림톡 발송 대상 리스트 호출
//    @Override
//    public List<KokonutUserAlimTalkFieldDto> selectUserAlimTalkList(String receiverNum, String appUserId, String searchQuery) {
//        return jdbcTemplate.query(
//                searchQuery,
//                (rs, rowNum) ->
//                        new KokonutUserAlimTalkFieldDto(
//                                receiverNum.isEmpty() ? null : rs.getObject(receiverNum),
//                                appUserId.isEmpty() ? null : rs.getObject(appUserId)
//                        )
//        );
//    }

}
