package com.app.kokonutuser.dtos;

import lombok.Getter;
import lombok.Setter;


/**
 * @author Woody
 * Date : 2023-01-03
 * Time :
 * Remark : 아직안쓰는중
 */
@Getter
@Setter
public class EncryptedPasswordData {

    private String encryptedPassword;
    private String salt;

    public EncryptedPasswordData(String encryptedPassword, String salt) {
        this.encryptedPassword = encryptedPassword;
        this.salt = salt;
    }

}
