package com.app.kokonut.provision.provisionentry.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * @author Woody
 * LocalDateTime : 2023-07-31
 * Time :
 * Remark : 개인정보제공할 항목 반환 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionEntryTargetsDto {

    private String pipeTableTargets; // 제공할 필드코드(,) 구분자

    public List<String> getPipeTableTargets() {
        return Arrays.asList(pipeTableTargets.split(","));
    }

}
