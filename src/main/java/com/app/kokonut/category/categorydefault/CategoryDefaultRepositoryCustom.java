package com.app.kokonut.category.categorydefault;

import com.app.kokonut.category.categorydefault.dtos.CategoryDefaultListSubDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-20
 * Time :
 * Remark : CategoryDefault Sql 쿼리호출
 */
public interface CategoryDefaultRepositoryCustom {

    List<CategoryDefaultListSubDto> findByCategoryDefaultList();

}