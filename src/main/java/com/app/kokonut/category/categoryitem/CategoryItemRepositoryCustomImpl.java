package com.app.kokonut.category.categoryitem;

import com.app.kokonut.category.categoryitem.dtos.CategoryItemListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-11
 * Time :
 * Remark : CategoryItemRepositoryCustomImpl 쿼리문 선언부
 */
@Repository
public class CategoryItemRepositoryCustomImpl extends QuerydslRepositorySupport implements CategoryItemRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public CategoryItemRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(CategoryItem.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public List<CategoryItemListDto> findByCategoryItemList(Long cdId) {

        QCategoryItem categoryItem = QCategoryItem.categoryItem;

        JPQLQuery<CategoryItemListDto> query = from(categoryItem)
                .where(categoryItem.cdId.eq(cdId))
                .leftJoin(categoryItem).on(categoryItem.cdId.eq(cdId))
                .select(Projections.constructor(CategoryItemListDto.class,
                        categoryItem.cddName,
                        categoryItem.cddSecurity,
                        new CaseBuilder()
                                .when(categoryItem.cddSubName.eq("모름")).then("")
                                .otherwise(categoryItem.cddSubName),
                        categoryItem.cddClassName
                ));

        return query.fetch();
    }


}
