package com.app.kokonutuser.dtos.use;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Woody
 * Date : 2023-07-12
 * Time :
 * Remark : 이메일발송할 대상의 리스트를 받아오는 Dto
 */
@Setter
@Getter
public class KokonutUserEmailFieldDto {

    private final Object emailField;

    public KokonutUserEmailFieldDto(Object emailField) {
        this.emailField = emailField;
    }

}
