package com.app.kokonut.provision.provisionlist.dtos;

import com.app.kokonut.common.ResponseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Woody
 * LocalDateTime : 2023-07-31
 * Time :
 * Remark : 개인정보를 제공할 개인정보명단 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionTargetIdxDto {

    private String piplTargetIdxs; // 제공할 kokonut_IDX 리스트(,) 구분자

    public String getPipeTableTargets() {
        return Arrays.asList(piplTargetIdxs.split(",")).stream()
                .map(Object::toString)
                .map(str -> "'" + str + "'") // 각 항목을 따옴표로 감싸줍니다.
                .collect(Collectors.joining(", "));
    }


}
