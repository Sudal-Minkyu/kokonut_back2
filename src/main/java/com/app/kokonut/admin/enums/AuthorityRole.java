package com.app.kokonut.admin.enums;

/**
 * @author Woody
 * Date : 2022-12-01
 * Time :
 * Remark : 사용자 권한 구분
 */
public enum AuthorityRole {

    ROLE_SYSTEM("ROLE_SYSTEM", "코코넛직원"), // 코코넛직원(우리)

    // 대표관리자
    // 1. 마우스오버하면 설명 달림
    // 2. 아이콘달린 관리자는 다른 최고관리자에게 권한을 넘겨줄 수 있다.
    // 2. 탈퇴할 수 있는 권한이있다.
    ROLE_MASTER("ROLE_MASTER", "최고관리자"), // 최고관리자(아이콘 있는 사람) -> 최고관리자, 일반관리자, 제3자 추가 할 수 있는 권한이 있는 사람 = 대표관리자

    ROLE_ADMIN("ROLE_ADMIN", "최고관리자"), // 최고관리자(아이콘 없는 사람) -> 일반관리자, 제3자 추가 할 수 있는 권한이 있는 사람

    ROLE_USER("ROLE_USER", "일반관리자"), // 일반관리자 -> 임시관리자를 추가 할 수 있는 권한이 있는 사람

    ROLE_GUEST("ROLE_GUEST","임시관리자"); // 임시관리자 -> 정보제공 다운로드 권한만 할 수 있는 사람

    private final String code;
    private final String desc;

    AuthorityRole(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getCodeByDesc(String desc) {
        for (AuthorityRole role : AuthorityRole.values()) {
            if (role.getDesc().equals(desc)) {
                return role.getCode();
            }
        }
        return null; // 해당하는 값이 없을 경우 null 반환
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
