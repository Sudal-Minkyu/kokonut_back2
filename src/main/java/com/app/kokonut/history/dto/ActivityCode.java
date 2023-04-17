package com.app.kokonut.history.dto;

/**
 * @author Woody
 * Date : 2023-01-09
 * Time :
 * Remark : 사용자 활동 Enum
 */
public enum ActivityCode {

    AC_01("AC_01", "로그인"),
    AC_02("AC_02", "회원정보 변경"),
    AC_03("AC_03", "회원정보 삭제"),
    AC_04("AC_04", "관리자 추가"),
    AC_05("AC_05", "관리자 권한 변경"),
    AC_06("AC_06", "열람이력 다운로드"),
    AC_07("AC_07", "활동이력 다운로드"),
    AC_08("AC_08", "고객정보 열람"),
    AC_09("AC_09", "고객정보 다운로드"),
    AC_10("AC_10", "고객정보 처리"),
    AC_11("AC_11", "개인정보 DB관리 변경"),
    AC_12("AC_12", "개인정보 DB항목 변경"),
    AC_13("AC_13", "개인정보 등록"),
    AC_14("AC_14", "정보제공 목록"),
    AC_15("AC_15", "정보파기 관리"),
    AC_16("AC_16", "개인정보 테이블 생성"),
    AC_17("AC_17", "전체 DB다운로드"),
    AC_18("AC_18", "개인정보 관리 변경"),

    AC_19("AC_19", "회원테이블 DB 항목 추가"),
    AC_20("AC_20", "회원테이블 DB 항목 수정"),
    AC_21("AC_21", "회원테이블 DB 항목 삭제"),

    AC_22("AC_22", "개인정보 전체 DB 데이터 다운로드 요청"),
    AC_23("AC_23", "개인정보 전체 DB 데이터 다운로드 시작"),

    AC_24("AC_24", "API KEY 발급"),
    AC_25("AC_25", "API KEY 재발급"),

//    AC_26("AC_26", "API KEY 사용 차단"),
//    AC_27("AC_27", "API KEY 사용 차단 해제"),

    AC_28("AC_28", "개인정보 처리방침 추가"),
    AC_29("AC_29", "개인정보 처리방침 삭제"),
    AC_30("AC_30", "개인정보 처리방침 수정"),
    AC_31("AC_31", "처리이력 다운로드"),
    AC_32("AC_32", "전체DB 다운로드"),

    AC_33("AC_33", "API KEY 허용IP 등록"),
    AC_34("AC_34", "API KEY 허용IP 삭제"),

    AC_35("AC_35", "휴대전화 번호 변경"),
    AC_36("AC_36", "소속명 변경"),
    AC_37("AC_37", "부서 변경/등록"),

    AC_38("AC_38", "비밀번호 변경"),

    AC_39("AC_39", "관리자 등록"),
    AC_40("AC_40", "관리자 목록 조회"),

    AC_41("AC_41", "1:1 문의등록하기"),

    ;
    
    private final String code;
    private final String desc;

    ActivityCode(String code, String desc) {
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
