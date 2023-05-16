package com.app.kokonut.privacyhistory.dtos;

/**
 * @author Woody
 * Date : 2023-05-15
 * Time :
 * Remark : 개인정보 처리및활동(생성,변경,삭제,조회,열람) Enum
 */
public enum PrivacyHistoryCode {

    PHC_01("PHC_01", "생성"), // 개인정보 생성
    PHC_02("PHC_02", "변경"), // 개인정보 변경
    PHC_03("PHC_03", "삭제"), // 개인정보 삭제
    PHC_04("PHC_04", "조회"), // 개인정보 조회
    PHC_05("PHC_05", "열람"), // 개인정보 열람
    ;
    
    private final String code;
    private final String desc;

    PrivacyHistoryCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
