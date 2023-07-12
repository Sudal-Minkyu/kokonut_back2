package com.app.kokonut.provision.provisionentry.dtos;

import lombok.Data;

import java.util.List;

/**
 * @author Woody
 * LocalDateTime : 2023-05-10
 * Time :
 * Remark : 정보제공 제공할 테이블과 그 테이블의 필드리스트를 받는 Dto
 */
@Data
public class ProvisionEntrySaveDto {

//    private String pipeTableName; // 제공할 테이블명

    private List<String> pipeTableTargets; // 제공할 필드코드(,) 구분자

}
