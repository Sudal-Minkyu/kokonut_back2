package com.app.kokonutdormant;

import com.app.kokonutdormant.dtos.KokonutDormantFieldCheckDto;
import com.app.kokonutdormant.dtos.KokonutDormantFieldInfoDto;
import com.app.kokonutdormant.dtos.KokonutDormantListDto;
import com.app.kokonutuser.dtos.KokonutRemoveInfoDto;
import com.app.kokonutuser.dtos.KokonutUserFieldDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-01-03
 * Time :
 * Remark : Kokonut-dormant DB & 테이블 - 쿼리문 선언부 모두 NativeQuery로 실행한다.
 */
@Slf4j
@Repository
public class DynamicDormantRepositoryCustomImpl implements DynamicDormantRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DynamicDormantRepositoryCustomImpl(@Qualifier("kokonutDormantJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 삭제테이블 생성/삭제/업데이트 메서드
    @Override
    public void dormantCommonTable(String commonQuery) {
        jdbcTemplate.execute(commonQuery);
    }

    public List<KokonutDormantListDto> findByDormantPage(String searchQuery) {
        return jdbcTemplate.query(
            searchQuery,
            (rs, rowNum) ->
                new KokonutDormantListDto(
                    rs.getLong("IDX"),
                    rs.getString("ID"),
                    rs.getTimestamp("REGDATE"),
                    rs.getTimestamp("LAST_LOGIN_DATE")
                )
        );
    }

    // 유저 등록여부 조회
    @Override
    public Integer selectDormantCount(String searchQuery) {
        return jdbcTemplate.queryForObject(searchQuery, Integer.class);
    }

    // 삭제할 휴면테이블 단일회원 조회
    @Override
    public List<KokonutRemoveInfoDto> selectDormantDataByIdx(String searchQuery) {
        return jdbcTemplate.query(searchQuery,
                new BeanPropertyRowMapper<>(KokonutRemoveInfoDto.class));
    }

    // 아이디 존재 유무 확인
    @Override
    public Integer selectDormantIdCheck(String searchQuery) {
        return jdbcTemplate.queryForObject(searchQuery, Integer.class);
    }

    // 휴면테이블의 컬럼 조회
    @Override
    public List<KokonutUserFieldDto> selectDormantColumns(String searchQuery) {
        return jdbcTemplate.query(searchQuery,
                new BeanPropertyRowMapper<>(KokonutUserFieldDto.class));
    }

    // 필드-값 쌍으로 사용자 컬럼값 조회
    @Override
    public List<KokonutDormantFieldInfoDto> selectDormantFieldList(String VALUE, String searchQuery) {
        return jdbcTemplate.query(
                searchQuery,
                (rs, rowNum) ->
                        new KokonutDormantFieldInfoDto(
                                rs.getLong("IDX"),
                                rs.getObject(VALUE)
                        )
        );
    }

    // 휴면테이블의 필드명을 통해 테이블명, 필드명 조회 -> 삭제하기위해 조회하는 메서드
    @Override
    public List<KokonutDormantFieldCheckDto> selectDormantTableNameAndFieldName(String searchQuery) {
        return jdbcTemplate.query(
                searchQuery,
                (rs, rowNum) ->
                        new KokonutDormantFieldCheckDto(
                                rs.getString("TABLE_NAME"),
                                rs.getString("COLUMN_NAME")
                        )
        );
    }

    // 휴면테이블 중복 체크 메서드
    @Override
    public int selectExistDormantTable(String companyCode) {
        String sql = "SELECT EXISTS (SELECT 1 FROM Information_schema.tables WHERE table_name = "+"'"+companyCode+"'"+") AS flag";
//        log.info("중복체크 sql : "+sql);
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    // kokonut_dormant 회원리스트 조회
    @Override
    public List<Map<String, Object>> selectDormantList(String searchQuery) {
        return jdbcTemplate.queryForList(searchQuery);
    }

}
