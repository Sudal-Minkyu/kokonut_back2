package com.app.kokonut.company.companytable.dtos;

import com.app.kokonutuser.dtos.KokonutUserFieldListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-13
 * Time :
 * Remark :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyTableListDto {

    private String ctName;

    private String ctDesignation;

    List<KokonutUserFieldListDto> kokonutUserFieldListDtos;

}
