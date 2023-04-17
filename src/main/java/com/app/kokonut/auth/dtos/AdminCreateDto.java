package com.app.kokonut.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Woody
 * Date : 2023-04-04
 * Time :
 * Remark : 관리자등록 -> 이메일 키 검증
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCreateDto {

    @NotBlank(message = "evKoData 값은 필수값 입니다.")
    private String evKoData;

    @NotBlank(message = "kvKoData 값은 필수값 입니다.")
    private String kvKoData;

    @NotBlank(message = "ivKoData 값은 필수값 입니다.")
    private String ivKoData;

}
