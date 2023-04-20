package com.app.kokonut.category.categorydefault;

import com.app.kokonut.category.categorydefault.dtos.CategoryDefaultListSubDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-11
 * Time :
 * Remark : CategoryDefaultRepositoryCustomImpl 쿼리문 선언부
 */
@Repository
public class CategoryDefaultRepositoryCustomImpl extends QuerydslRepositorySupport implements CategoryDefaultRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public CategoryDefaultRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(CategoryDefault.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    // 기본 카테고리항목 가져오기
    @Override
    public List<CategoryDefaultListSubDto> findByCategoryDefaultList() {

        QCategoryDefault categoryDefault = QCategoryDefault.categoryDefault;

        JPQLQuery<CategoryDefaultListSubDto> query = from(categoryDefault)
                .select(Projections.constructor(CategoryDefaultListSubDto.class,
                        categoryDefault.cdId,
                        categoryDefault.cdName
                ));

        query.orderBy(categoryDefault.cdId.asc());

        return query.fetch();
    }


}
