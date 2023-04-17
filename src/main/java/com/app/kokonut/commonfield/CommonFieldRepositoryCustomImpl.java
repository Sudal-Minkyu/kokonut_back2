package com.app.kokonut.commonfield;

import com.app.kokonut.commonfield.dtos.CommonFieldDto;
import com.app.kokonut.history.History;
import com.app.kokonut.history.dto.Column;
import lombok.extern.slf4j.Slf4j;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Woody
 * Date : 2022-12-27
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class CommonFieldRepositoryCustomImpl extends QuerydslRepositorySupport implements CommonFieldRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public CommonFieldRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(Object.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    // 기본테이블을 생성하는데 필요한 컬럼가져오기
    @Override
    public List<CommonFieldDto> selectCommonUserTable() {

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        // 네이티브 쿼리문
        sb.append("SHOW FULL COLUMNS FROM common_default \n");

        // 쿼리조건 선언부
        Query query = em.createNativeQuery(sb.toString());

        return jpaResultMapper.list(query, CommonFieldDto.class);
    }

}
