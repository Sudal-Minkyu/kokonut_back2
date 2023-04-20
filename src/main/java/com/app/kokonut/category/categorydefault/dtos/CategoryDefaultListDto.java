package com.app.kokonut.category.categorydefault.dtos;

import com.app.kokonut.category.categoryitem.dtos.CategoryItemListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-20
 * Time :
 * Remark :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDefaultListDto {

    private String cdName;

    private List<CategoryItemListDto> categoryItemListDtoList;

}
