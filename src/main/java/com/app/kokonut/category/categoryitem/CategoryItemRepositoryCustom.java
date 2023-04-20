package com.app.kokonut.category.categoryitem;

import com.app.kokonut.category.categorydefault.dtos.CategoryDefaultListDto;
import com.app.kokonut.category.categoryitem.dtos.CategoryItemListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-20
 * Time :
 * Remark : CategoryDefault Sql 쿼리호출
 */
public interface CategoryItemRepositoryCustom {

    List<CategoryItemListDto> findByCategoryItemList(Long cdId);

}