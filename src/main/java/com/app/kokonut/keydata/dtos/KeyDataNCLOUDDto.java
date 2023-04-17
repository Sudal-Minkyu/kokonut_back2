package com.app.kokonut.keydata.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2022-01-08
 * Time :
 * Remark : AWS NCLOUD 키 반환하는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyDataNCLOUDDto {

    private String NCLOUDSERVICEID;

    private String NCLOUDSERVICEACCESS;

    private String NCLOUDSERVICESECRET;

    private String NCLOUDSERVICEPRIMARY;

    private String NCLOUDSERVICECATEGORY;

}
