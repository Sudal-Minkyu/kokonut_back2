package com.app.kokonut.common;

/**
 * @author Woody
 * Date : 2022-11-07
 * Time :
 * Remark : 에러 응답코드, 응답내용
 */
public enum ResponseErrorCode {

    KO000("KO000", "체험하기모드는 이용할 수 없습니다."),
    KO001("KO001", "현재 페이지 혹은 기능의 사용 권한이 있는 계정으로 로그인이 필요합니다."),
    KO002("KO002", "조회할 수 없습니다."),
    KO003("KO003", "조회한 데이터가 없습니다."),
    KO004("KO004", "존재하지 않습니다."),
    KO005("KO005", "이미 가입되어 있는 이메일입니다."),
    KO006("KO006", "유효하지 않은 토큰정보입니다. 다시 로그인 해주세요."),
    KO007("KO007", "Refresh Token 정보가 유효하지 않습니다."),
//    KO008("KO008", "Refresh Token 정보가 일치하지 않습니다."),
    KO009("KO009", "사용하실 수 없는 토큰정보 입니다. 다시 로그인 해주세요."),
    KO010("KO010", "구글 OTP 값이 존재하지 않습니다."),
    KO011("KO011", "구글 OTP 인증을 등록해주세요."),
    KO012("KO012", "입력된 구글 OTP 값이 일치하지 않습니다. 다시 확인해주세요."),
    KO013("KO013", "입력하신 비밀번호가 일치하지 않습니다."),
    KO014("KO014", "인증되지 않은 절차로 진행되었습니다. 올바른 방법으로 진행해주세요."),
    KO015("KO015", "OTP는 숫자 형태로 입력하세요."),
    KO016("KO016", "아이디 또는 비밀번호가 일치하지 않습니다."),
    KO017("KO017", "채널ID를 입력해주세요."),
    KO018("KO018", "전화번호를 입력해주세요."),
    KO019("KO019", "인증번호를 입력해주세요."),
    KO020("KO020", "카카오 채널 삭제를 실패했습니다."),
    KO021("KO021", "404 에러 NCP에서 템플릿 삭제됬는지 확인해볼 것 to.woody"),
    KO022("KO022", "알림톡 템플릿 삭제를 실패했습니다."),
    KO023("KO023", "해당 알림톡 메세지를 찾을 수 없습니다."),
    KO024("KO024", "예약발송일 경우 보낼시간을 설정해주세요."),
    KO025("KO025", "발송할 수신자를 선택해주세요."),
    KO026("KO026", "알림톡 발송을 실패했습니다."),
    KO027("KO027", "알림톡메세지 결과정보가 존재하지 않습니다."),
    KO028("KO028", "예약발송 취소를 실패했습니다."),
    KO029("KO029", "해당 친구톡 메세지를 찾을 수 없습니다."),
    KO030("KO030", "친구톡 이미지 업로드를 실패했습니다."),
    KO031("KO031", "이메일 상세정보 ID 값이 존재하지 않습니다."),
    KO032("KO032", "회원가입 정보가 존재 하지 않습니다."),

    KO033("KO033", "본인인증으로 입력된 핸드폰 번호가 아닙니다. 본인인증을 완료해주세요."),
    KO034("KO034", "이미 회원가입된 핸드폰번호 입니다."),
    KO035("KO035", "이미 회원가입된 사업자등록번호 입니다."),
    KO036("KO036", "암호화 생성을 실패했습니다. 관리자에게 문의해주세요."),
//    KO037("KO037", "입력하신 비밀번호가 서로 일치하지 않습니다."),
    KO038("KO038", "사업자등록증을 업로드해주세요."),
    KO039("KO039", "이미지 업로드를 실패했습니다. 관리자에게 문의해주세요."),

    KO040("KO040", "이메일 받는 사람 유형이 입력되지 않았습니다. 관리자에게 문의해주세요."),
    KO041("KO041", "이메일 발송을 실패했습니다. 관리자에게 문의해주세요."),
    KO042("KO042", "이메일 그룹 ID 값이 존재하지 않습니다."),

    KO043("KO043", "암호화 키가 존재하지 않습니다."),
    KO044("KO044", "개인정보 상태 여부를 선택해주세요."),
    KO045("KO045", "의 필드명은 존재하지 않습니다. 새로고침이후 다시 시도해주시길 바랍니다."),
    KO046("KO046", "이미 사용중인 개인정보 ID 입니다."),

    KO047("KO047", "요청하신 공지사항이 존재하지 않습니다. 새로고침 이후 다시 시도해 주시기를 바랍니다."),
    KO048("KO048", "요청하신 공지사항의 조회를 실패하였습니다. 새로고침 이후 다시 시도해 주시기를 바랍니다."),

    KO049("KO049", "요청하신 질문이 존재하지 않습니다. 새로고침 이후 다시 시도해 주시기를 바랍니다."),
    KO050("KO050", "요청하신 질문의 조회를 실패하였습니다. 새로고침 이후 다시 시도해 주시기를 바랍니다."),

    KO051("KO051", "요청하신 개인정보 수집 및 이용 안내가 존재하지 않습니다. 새로고침 이후 다시 시도해 주시기를 바랍니다."),
    KO052("KO052", "요청하신 개인정보 수집 및 이용 안내의 조회를 실패하였습니다. 새로고침 이후 다시 시도해 주시기를 바랍니다."),

    KO053("KO053", "요청하신 1:1 문의가 존재하지 않습니다. 새로고침 이후 다시 시도해 주시기를 바랍니다."),
    KO054("KO054", "요청하신 1:1 문의의 조회를 실패하였습니다. 새로고침 이후 다시 시도해 주시기를 바랍니다."),
    KO055("KO055", "본인이 작성한 1:1 문의만 확인 가능합니다."),

    KO056("KO056", "파일 업로드를 실패했습니다. 관리자에게 문의해주세요."),

    KO057("KO057", "요청하신 서비스의 ID 값이 존재하지 않습니다."),

    KO058("KO058", "존재하지 않은 개인정보입니다. 새로고침이후 다시 시도해주세요."),
    KO059("KO059", "문제가 발생하였습니다. 새로고침이후 다시 시도해주세요."),
    KO060("KO060", "개인정보 상태를 선택해 주세요."),
    KO061("KO061", "검사 할 엑셀파일을 선택 해 주세요."),
    KO062("KO062", "읽을 수 없는 엑셀파일 입니다."),
    KO063("KO063", "IDX 컬럼은 저장할 수 없습니다."),
    KO064("KO064", "이미 존재하는 컬럼명 입니다."),
    KO065("KO065", "이미 존재하는 개인정보 항목입니다."),
    KO066("KO066", "입력하신 사업자등록번호는 10자리가 아닙니다."),
    KO067("KO067", "수정할 항목이 테이블에 존재하지 않습니다."),
    KO068("KO068", "개인정보 데이터 다운로드 요청에 실패 했습니다. 관리자에게 문의해주세요."),
    KO069("KO069", "개인정보 데이터 다운로드 요청 데이터가 존재하지 않습니다. 관리자에게 문의해주세요."),
    KO070("KO070", "개인정보 다운로드 가능 횟수를 초과했습니다."),
    KO071("KO071", "이미 발급된 API KEY가 존재합니다. 재발급을 진행해주세요."),
    KO072("KO072", "제3자 제공일 경우 정보제공 동의여부를 체크해주세요."),
    KO073("KO073", "정보제공 등록에 실패했습니다. 관리자에게 문의해주세요."),

    KO074("KO074", "최소 한개 이상의 IP 설정은 필수입니다."),
    KO075("KO075", "제한시간 내에 입력하시지 않았습니다. 다시 인증번호를 받아주세요."),
    KO076("KO076", "입력하신 인증번호가 일치하지 않습니다. 다시 입력해주세요."),

    KO077("KO077", "활동날짜 범위를 지정해주세요."),

    KO078("KO078", "보내드린 임시 비밀번호와 일치하지 않습니다."),
    KO079("KO079", "이메일을 입력해주세요."),
    KO080("KO080", "등록하신 API Key가 존재하지 않습니다. API Key 먼저 발급해주시길 바랍니다."),
    KO081("KO081", "등록하신 API Key의 허용 IP 갯수를 초과하였습니다."),
    KO082("KO082", "삭제하실 허용IP가 존재하지 않습니다."),
    KO083("KO083", "새로운 비밀번호가 일치하지 않습니다."),

    KO084("KO084", "이메일 인증이 맞지 않습니다."),
    KO085("KO085", "만료된 페이지 입니다."),
    KO086("KO086", "이미 가입된 관리자 입니다."),

    KO087("KO087", "이미 등록되어 있는 항목입니다."),
    KO088("KO088", "이미 등록되어 있는 테이블명입니다."),
    KO089("KO089", "추가할 테이블명을 입력해주세요."),

    KO090("KO090", "요청하신 개인정보처리방침이 존재하지 않습니다. 새로고침이후 진행해주세요."),

    KO091("KO091", "요청하신 항목이 존재하지 않습니다. 새로고침이후 진행해주세요."),

    KO092("KO092", "개인정보 제공등록을 실패했습니다. 새로고침이후 진행해주세요."),
    KO093("KO093", "제공할 개인정보가 존재하지 않습니다. 제공할 개인정보를 선택해주세요."),

    KO094("KO094", "접속 허용되지 않은 IP 입니다. 관리자에게 등록을 요청해주세요."),
    KO095("KO095", "일반관리자용 : 비밀번호를 x회 틀리셨습니다. 관리자에게 비밀번호변경을 문의하시길 바랍니다."),
    KO096("KO096", "마스터용 : 비밀번호를 x회 틀리셨습니다. contact@kokonut.me로 문의바랍니다."),

    KO097("KO097", "카드 키를 조회할 수 없습니다. 코코넛관리자에게 문의해주세요."),


    ERROR_CODE_00("ERROR_CODE_00", "파라메터 데이터가 없습니다."),
    ERROR_CODE_01("ERROR_CODE_01", "지정되지 않은 값입니다. 고유코드를 확인해주시고 메뉴얼대로 호출해주시길 바랍니다."),
    ERROR_CODE_02("ERROR_CODE_02", "존재하지 않은 테이블입니다. 존재하는 고유코드인지 확인해주세요."),
    ERROR_CODE_03("ERROR_CODE_03", "필수항목을 넣지 않거나, 중복전송을 하셨습니다. '1_id'와'1_pw' 는 하나씩만 보내주시길 바랍니다."),
    ERROR_CODE_04("ERROR_CODE_04", "존재하지 않은 고유코드 입니다. 고유코드를 확인 해주세요."),
    ERROR_CODE_05("ERROR_CODE_05", "휴대전화번호는 '-'를 뺀 형태로 보내주시길 바랍니다."),
    ERROR_CODE_06("ERROR_CODE_06", "중복되는 고유코드가 존재합니다. 중복되지 않도록 고유코드를 보내주세요."),
    ERROR_CODE_07("ERROR_CODE_07", "조회하고자 하는 파라메터 값이 일정하지 않습니다. 보내시는 파라메터 값을 다시 한번 확인해주세요."),
    ERROR_CODE_08("ERROR_CODE_08", "조회하실 파라메터가 존재하지 않습니다. 보내시는 파라메터 값을 추가해주세요."),
    ERROR_CODE_09("ERROR_CODE_09", "이메일주소 형식과 맞지 않습니다. 다시 한번 확인해주시길 바랍니다."),


    ERROR_CODE_96("ERROR_CODE_96", "헤더에 APIKey가 존재하지 않습니다. APIKey를 담아 보내주세요."), // 400
    ERROR_CODE_97("ERROR_CODE_97", "호출하신 APIKey는 존재하지 않은 APIKey 입니다. APIKey관리 페이지에서 APIKey를 확인해주세요."), // 404
    ERROR_CODE_98("ERROR_CODE_98", "관리자에 의해 사용에 제한된 APIKey 입니다. 관리자에게 문의해주세요."), // 402
    ERROR_CODE_99("ERROR_CODE_99", "허용되지 않은 IP 입니다. APIKey관리 페이지에서 허용IP를 추가해주세요."), // 403
    ;

    private String code;
    private String desc;

    ResponseErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
