package com.app.kokonutapi.auth;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.awskmshistory.dto.AwsKmsResultDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.AESGCMcrypto;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.common.realcomponent.Utils;
import com.app.kokonut.company.companydatakey.CompanyDataKeyService;
import com.app.kokonut.history.extra.apicallhistory.ApiCallHistoryService;
import com.app.kokonut.history.extra.decrypcounthistory.DecrypCountHistoryService;
import com.app.kokonut.history.extra.encrypcounthistory.EncrypCountHistoryService;
import com.app.kokonut.privacyhistory.PrivacyHistoryService;
import com.app.kokonut.privacyhistory.dtos.PrivacyHistoryCode;
import com.app.kokonutapi.auth.dtos.AuthApiLoginDto;
import com.app.kokonutuser.DynamicUserRepositoryCustom;
import com.app.kokonutuser.KokonutUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author Woody
 * Date : 2023-04-27
 * Time :
 * Remark :
 */
@Slf4j
@Service
public class AuthApiService {

    private static final String COLUMN_SEP_TYPE = "||__||"; // 암호화항목의 대한 DB 구분자

    private final AdminRepository adminRepository;
    private final CompanyDataKeyService companyDataKeyService;

    private final KokonutUserService kokonutUserService;
    private final PrivacyHistoryService privacyHistoryService;
    private final DynamicUserRepositoryCustom dynamicUserRepositoryCustom;

    private final ApiCallHistoryService apiCallHistoryService;
    private final EncrypCountHistoryService encrypCountHistoryService;
    private final DecrypCountHistoryService decrypCountHistoryService;

    @Autowired
    public AuthApiService(AdminRepository adminRepository, CompanyDataKeyService companyDataKeyService, KokonutUserService kokonutUserService, PrivacyHistoryService privacyHistoryService, DynamicUserRepositoryCustom dynamicUserRepositoryCustom, ApiCallHistoryService apiCallHistoryService, EncrypCountHistoryService encrypCountHistoryService, DecrypCountHistoryService decrypCountHistoryService){
        this.adminRepository = adminRepository;
        this.companyDataKeyService = companyDataKeyService;
        this.dynamicUserRepositoryCustom = dynamicUserRepositoryCustom;
        this.kokonutUserService = kokonutUserService;
        this.privacyHistoryService = privacyHistoryService;
        this.apiCallHistoryService = apiCallHistoryService;
        this.encrypCountHistoryService = encrypCountHistoryService;
        this.decrypCountHistoryService = decrypCountHistoryService;
    }

    // API용 개인정보(고객의 고객) 로그인
    public ResponseEntity<Map<String, Object>> apiLogin(AuthApiLoginDto authApiLoginDto, JwtFilterDto jwtFilterDto) {
        log.info("apiLogin 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("authApiLoginDto : "+authApiLoginDto);
        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
//        Long adminId = adminCompanyInfoDto.getAdminId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();
//        log.info("adminId : "+adminId);
//        log.info("companyCode : "+companyCode);
        String basicTable = companyCode+"_1";

        if(authApiLoginDto.getKokonutId() == null) {
            return ResponseEntity.ok(res.apifail("KOKONUT_01", "아이디를 입력해주세요."));
        }

        String result = kokonutUserService.passwordConfirm(basicTable, authApiLoginDto.getKokonutId(), authApiLoginDto.getKokonutPw());
        log.info("result : "+result);
        data.put("IDX", result);

        if(result.equals("none")) {
            return ResponseEntity.ok(res.apifail("KOKONUT_03", "입력한 비밀번호가 맞지 않습니다."));
        } else if(result.equals("")) {
            return ResponseEntity.ok(res.apifail("KOKONUT_02", "존재하지 않은 아이디 입니다."));
        } else {
            return ResponseEntity.ok(res.apisuccess(data));
        }

    }

    // API용 개인정보(고객의 개인정보) 회원가입
    @Transactional
    public ResponseEntity<Map<String, Object>> apiRegister(HashMap<String, Object> paramMap, JwtFilterDto jwtFilterDto) throws Exception {
        log.info("apiRegister 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("paramMap : "+paramMap);

        String kphReason = ""; // 처리사유
        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        Long adminId = adminCompanyInfoDto.getAdminId();
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        int echCount = 0; // 암호화 카운팅

        AwsKmsResultDto awsKmsResultDto = companyDataKeyService.findByCompanyDataKey(cpCode);
//        log.info("DataKey : "+awsKmsResultDto.getDataKey());
//        log.info("IV : "+awsKmsResultDto.getIvKey());

        // 해당 데이터 암호화
//        String name = "test12345";
//        String testData = AESGCMcrypto.encrypt(name.getBytes(StandardCharsets.UTF_8), awsKmsResultDto.getSecretKey(), Base64.getDecoder().decode(awsKmsResultDto.getIvKey()));
//        log.info("암호화 testData : "+testData);
//        if(testData.equals("g2kBEwzaAJ1q9xQPnlDaWg9BWJWp0CDhsg==")) {
//            log.info("암호화가 일치합니다.");
//        } else {
//            log.info("암호화가 일치하지 않습니다.");
//        }
//        String testData2 = AESGCMcrypto.decrypt(testData, awsKmsResultDto.getSecretKey(), awsKmsResultDto.getIvKey());
//        log.info("복호화 testData2 : "+testData2);

        int trigger = 0;
        if(paramMap != null) {
            // 받은값 정렬
            Map<Integer, List<Map.Entry<String, Object>>> sortedGroupedMap = new TreeMap<>();
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                String[] parts = entry.getKey().split("_");
                if (parts.length != 2) {
                    throw new NumberFormatException(entry.getKey());
                }
                try {
                    int numKey = Integer.parseInt(parts[0]);
                    String objKey = parts[1];
                    if (!objKey.equals("id") && !objKey.equals("pw") && !objKey.matches("-?\\d+(\\.\\d+)?")) {
                        throw new NumberFormatException(entry.getKey());
                    }
                    if (!sortedGroupedMap.containsKey(numKey)) {
                        sortedGroupedMap.put(numKey, new ArrayList<>());
                    }
                    sortedGroupedMap.get(numKey).add(entry);
                } catch (NumberFormatException e) {
                    throw new NumberFormatException(entry.getKey());
                }
            }

            log.info("sortedGroupedMap: " + sortedGroupedMap);

            // 실행할 인서트쿼리 담는 리스트
            List<String> queryList = new ArrayList<>();
            StringBuilder searchQuery;

            // 난수 생성
            String kokonutIdx = Utils.getRamdomStr(20);
//            log.info("생성된 난수 : "+kokonutIdx);

            // 정렬된 값을 통해 회원가입을 한다.
            for (Map.Entry<Integer, List<Map.Entry<String, Object>>> groupEntry : sortedGroupedMap.entrySet()) {

                String groupKey = String.valueOf(groupEntry.getKey());
                // 회원가입저장하는데 맨앞 키가 "1"이 아닐 경우 에러를 던저준다.
                // 내용 : 회원가입 할 경우에만 호출할 수 있는 테이블입니다. 회원가입에 필요한 필수사항을 살펴보신 후 호출하신 고유코드를 재확인 해주시길 바랍니다.
                if(trigger == 0 && !groupKey.equals("1")) {
                    // 내용 : 회원가입 고유코드가 하나라도 존재하지 않을 경우
                    log.error("");
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_01.getCode(),ResponseErrorCode.ERROR_CODE_01.getDesc()));
                }

                String saveTable = cpCode+"_"+groupKey;
                log.info("그룹키 : " + groupKey);
                log.info("컬럼 조회할 테이블 : "+saveTable);

                searchQuery = new StringBuilder();
                searchQuery.append("INSERT INTO `").append(saveTable).append("` (kokonut_IDX, kokonut_REGISTER_DATE, kokonut_REGDATE, ");

                List<Map.Entry<String, Object>> groupValues = groupEntry.getValue();

                if(groupKey.equals("1")) {
                    // 필수항목 검증 : 필수사항의 항목은 "1_id" 와 "1_pw" 입니다.
                    for (Map.Entry<String, Object> entry : groupValues) {
//                        log.info("키 : "+entry.getKey());
                        if(String.valueOf(entry.getKey()).equals("1_id") || String.valueOf(entry.getKey()).equals("1_pw")) {
                            trigger++;
                        }
                    }
                    // trigger 이 "2"가 아닐경우 에러를 던저준다.
                    if(trigger != 2) {
                        // 내용 : 필수항목을 넣지 않거나, 중복전송을 하셨습니다. "1_id"와"1_pw"는 하나씩만 보내주시길 바랍니다.
                        log.error("필수항목을 넣지 않거나, 중복전송을 하셨습니다. '1_id'와 '1_pw'는 하나씩만 보내주시길 바랍니다.");
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_03.getCode(),ResponseErrorCode.ERROR_CODE_03.getDesc()));
                    }
                }

                // 존재하는 테이블인지 검증하는 절차 : cpCode와 해당 그룰키를 통해 해당 테이블의 컬럼값을 조회한다.
                int verifiResult= kokonutUserService.getTableVerification(saveTable);
                log.info("verifiResult : "+verifiResult);

                if(verifiResult == 0) {
                    log.error("존재하지 않은 테이블입니다. 존재하는 고유코드인지 확인해주세요.");
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_02.getCode(),ResponseErrorCode.ERROR_CODE_02.getDesc()+" 고유코드 : "+groupEntry.getValue()));
                }
                else {
                    log.info("인서트 시작");

                    // 컬럼존재 검증절차 : 테이블의 컬럼 조회
//                    List<KokonutUserFieldDto> kokonutUserFieldDtos = kokonutUserService.getColumns(saveTable);
//                    log.info("kokonutUserFieldDtos : "+kokonutUserFieldDtos);

                    // 컬럼존재 검증 절차
                    List<Map<String, Object>> tableComments = kokonutUserService.getCommentOrEncrypt(saveTable);
                    log.info("tableComments : "+tableComments);

                    List<String> names = new ArrayList<>();
                    List<String> encrypts = new ArrayList<>(); // 암호화여부

                    int confirm;
                    // 컬럼 존재검증
                    for (int i=0; i<groupValues.size(); i++) {
                        confirm = 0;

                        String checkColumn;
                        String key = String.valueOf(groupValues.get(i).getKey());
                        if(key.equals("1_id")) {
                            checkColumn = "ID_" + key;
                        } else if(key.equals("1_pw")) {
                            checkColumn = "PASSWORD_" + key;
                        } else {
                            checkColumn = cpCode + "_" + key;
                        }

//                        log.info("인서트 및 검증할 checkColumn : " + checkColumn);

                        for(Map<String, Object> column : tableComments) {
                            String field = (String) column.get("Field");
//                            log.info("field : " + field);

                            if(!field.contains("kokonut_")) {
                                String comment = String.valueOf(column.get("Comment"));
//                                log.info("comment : " + comment);

                                String[] commentText = comment.split(",");
                                if (commentText[5].equals(key)) {
                                    confirm = 1;
                                    if (i == groupValues.size() - 1) {
                                        searchQuery.append(checkColumn).append(") ");
                                    } else {
                                        searchQuery.append(checkColumn).append(", ");
                                    }
                                    names.add(commentText[0]); // 커맨트명
                                    encrypts.add(commentText[1]); // 암호화여부
                                    break;
                                }
                            }
                        }

                        if (confirm == 0) {
                            log.error("존재하지 않은 고유코드 입니다. 고유코드를 확인 해주세요. 고유코드 : " + key);
                            return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_04.getCode(),
                                    ResponseErrorCode.ERROR_CODE_04.getDesc() + " 고유코드 : " + key));
                        }
                    }

                    log.info("names : " + names);
                    log.info("encrypts : " + encrypts);

                    searchQuery.append("VALUES ('").append(kokonutIdx).append("', NOW(), NOW(), ");
                    for(int i=0; i<groupValues.size(); i++) {
                        String key = String.valueOf(groupValues.get(i).getKey());
                        String value = String.valueOf(groupValues.get(i).getValue());
                        if(key.equals("1_id")) {
                            // 아이디일 경우 중복체크를 한다.
                            boolean result = kokonutUserService.isUserExistId(saveTable, value);
                            if(result) {
                               // result가 true일 경우 존재한다고 판단
                                log.error("이미 사용중인 아이디입니다.");
                                return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_10.getCode(),ResponseErrorCode.ERROR_CODE_10.getDesc()));
                            } else {
                                kphReason = value.charAt(0) + Utils.starsForString(value) + value.substring(value.length() - 1)+" 님의 개인정보 생성";
                            }
                        }
                        if (encrypts.get(i).equals("암호화")) {

                            // -> 이름은 통으로 암호화로 수정 -> 2023.06.09
                            // -> 앞과 뒤를 제외한 가운데만 암호화로 다시변경 -> 2023.07.10
                            if(names.get(i).equals("이름")) {
                                // 이름의 데이터의 대한 암호화
                                int nameLength = value.length();

                                // 이름이 한글자임
                                if(nameLength == 1)  {
                                    value = AESGCMcrypto.encrypt(value.getBytes(StandardCharsets.UTF_8), awsKmsResultDto.getSecretKey(),
                                                    Base64.getDecoder().decode(awsKmsResultDto.getIvKey()));
                                } else {
                                    String middleName;
                                    String lastName;
                                    if (nameLength % 2 == 0) {
                                        if(nameLength == 2) {
                                            // 이름이 두글자 일 경우
                                            middleName = value.substring(1,2);
                                            lastName = "";
                                        } else {
                                            middleName = value.substring(nameLength / 2 - 1, nameLength / 2 + 1); // 이름이 짝수 글자일 때
                                            lastName = value.substring(nameLength - 1);
                                        }
                                    } else {
                                        middleName = value.substring(nameLength / 2, nameLength / 2 + 1); // 이름이 홀수 글자일 때
                                        lastName = value.substring(nameLength - 1);
                                    }

                                    log.info("middleName : "+middleName);

                                    value = value.charAt(0)+ COLUMN_SEP_TYPE +
                                            AESGCMcrypto.encrypt(middleName.getBytes(StandardCharsets.UTF_8), awsKmsResultDto.getSecretKey(),
                                                    Base64.getDecoder().decode(awsKmsResultDto.getIvKey())) + COLUMN_SEP_TYPE +
                                            lastName;
                                }
                            }

                            else if (names.get(i).equals("휴대전화번호")) {
                                // 휴대전화번호 데이터의 대한 암호화
                                if(value.length() == 11) {
                                    value = value.substring(0,3) + COLUMN_SEP_TYPE +
                                            AESGCMcrypto.encrypt(value.substring(3, 7).getBytes(StandardCharsets.UTF_8), awsKmsResultDto.getSecretKey(),
                                                    Base64.getDecoder().decode(awsKmsResultDto.getIvKey())) + COLUMN_SEP_TYPE +
                                            value.substring(7,11);
                                } else {
                                    log.error(names.get(i)+" 형식과 맞지 않습니다. (-)를 제거하고 보내주세요. 보내신 "+
                                            names.get(i)+ " : " + value);
                                    return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_11.getCode(),
                                            names.get(i)+ResponseErrorCode.ERROR_CODE_11.getDesc()+" 보내신 "+
                                                    names.get(i)+ " : " + value));
                                }
                            }

                            else if (names.get(i).equals("연락처")) {
                                // 연락처의 데이터의 대한 암호화
                                if(value.length() == 9 || value.length() == 10 || value.length() == 11) {
                                    if(value.length() == 9) {
                                        // 가운데 3자리만 암호화
                                        value = value.substring(0,2) + COLUMN_SEP_TYPE +
                                                AESGCMcrypto.encrypt(value.substring(2, 5).getBytes(StandardCharsets.UTF_8), awsKmsResultDto.getSecretKey(),
                                                        Base64.getDecoder().decode(awsKmsResultDto.getIvKey())) + COLUMN_SEP_TYPE +
                                                value.substring(5,9);
                                    } else if(value.length() == 10) {
                                        // 가운데 4자리만 암호화
                                        if(value.startsWith("02")) {
                                            value = value.substring(0,2) + COLUMN_SEP_TYPE +
                                                    AESGCMcrypto.encrypt(value.substring(2, 6).getBytes(StandardCharsets.UTF_8), awsKmsResultDto.getSecretKey(),
                                                            Base64.getDecoder().decode(awsKmsResultDto.getIvKey())) + COLUMN_SEP_TYPE +
                                                    value.substring(6,10);
                                        } else {
                                            value = value.substring(0,3) + COLUMN_SEP_TYPE +
                                                    AESGCMcrypto.encrypt(value.substring(3, 6).getBytes(StandardCharsets.UTF_8), awsKmsResultDto.getSecretKey(),
                                                            Base64.getDecoder().decode(awsKmsResultDto.getIvKey())) + COLUMN_SEP_TYPE +
                                                    value.substring(6,10);
                                        }
                                    } else{
                                        value = value.substring(0,3) + COLUMN_SEP_TYPE +
                                                AESGCMcrypto.encrypt(value.substring(3, 7).getBytes(StandardCharsets.UTF_8), awsKmsResultDto.getSecretKey(),
                                                        Base64.getDecoder().decode(awsKmsResultDto.getIvKey())) + COLUMN_SEP_TYPE +
                                                value.substring(7,11);
                                    }
                                } else {
                                    log.error(names.get(i)+" 형식과 맞지 않습니다. (-)를 제거하고 보내주세요. 보내신 "+
                                            names.get(i)+ " : " + value);
                                    return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_11.getCode(),
                                            names.get(i)+ResponseErrorCode.ERROR_CODE_11.getDesc()+" 보내신 "+
                                                    names.get(i)+ " : " + value));
                                }
                            }

                            // 이메일주소는 통으로 암호화로 수정 -> 2023.06.09
                            // -> 앞에 이메일아이디는 앞과 뒤를 제외한 가운데만 암호화 뒤에 도메인은 그대로 -> 2023.07.10
                            else if (names.get(i).equals("이메일주소")) {
                                String[] emailAddress = value.split("@");
                                log.info("emailAddress : "+ Arrays.toString(emailAddress));
                                log.info("emailAddress.length : "+ emailAddress.length);
                                if(emailAddress.length == 2) {
                                    value = AESGCMcrypto.encrypt(emailAddress[0].getBytes(StandardCharsets.UTF_8), awsKmsResultDto.getSecretKey(),
                                            Base64.getDecoder().decode(awsKmsResultDto.getIvKey())) + COLUMN_SEP_TYPE+"@" +
                                            emailAddress[1];
                                } else {
                                    log.error("이메일주소 형식과 맞지 않습니다. 다시 한번 확인해주시길 바랍니다. 보내신 이메일주소 : " + value);
                                    return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_09.getCode(),
                                            ResponseErrorCode.ERROR_CODE_09.getDesc() + " 보내신 이메일주소 : " + value));
                                }
                            }

                            // 주민등록번호 or 거소신고번호 or 외국인등록번호
                            else if (names.get(i).equals("주민등록번호") || names.get(i).equals("거소신고번호") || names.get(i).equals("외국인등록번호")) {
                                if(value.length() == 13) {
                                    value = value.substring(0,6)+COLUMN_SEP_TYPE+AESGCMcrypto.encrypt(value.substring(6).getBytes(StandardCharsets.UTF_8), awsKmsResultDto.getSecretKey(),
                                            Base64.getDecoder().decode(awsKmsResultDto.getIvKey()));
                                } else {
                                    log.error(names.get(i)+" 형식과 맞지 않습니다. (-)를 제외하여 보내주시길 바랍니다. 보내신 "+names.get(i)+" : " + value);
                                    return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_11.getCode(),
                                            names.get(i)+ResponseErrorCode.ERROR_CODE_11.getDesc() + " 보내신 "+names.get(i)+" : " + value));
                                }
                            }

                            // 운전면허번호 대한민국 : 12자리, 그외 외국 : 7~15자리
                            else if (names.get(i).equals("운전면허번호")) {
                                value = value.substring(0,6)+COLUMN_SEP_TYPE+AESGCMcrypto.encrypt(value.substring(6).getBytes(StandardCharsets.UTF_8), awsKmsResultDto.getSecretKey(),
                                        Base64.getDecoder().decode(awsKmsResultDto.getIvKey()));
                            }

                            // 여권번호 8~9자리
                            else if (names.get(i).equals("여권번호")) {
                                value = value.substring(0,4)+COLUMN_SEP_TYPE+AESGCMcrypto.encrypt(value.substring(4).getBytes(StandardCharsets.UTF_8), awsKmsResultDto.getSecretKey(),
                                        Base64.getDecoder().decode(awsKmsResultDto.getIvKey()));
                            }

                            // 일반 데이터항목 암호화하기
                            else {
                                value = AESGCMcrypto.encrypt(value.getBytes(StandardCharsets.UTF_8), awsKmsResultDto.getSecretKey(),
                                        Base64.getDecoder().decode(awsKmsResultDto.getIvKey()));
                            }
                            log.info("암호화 데이터 : " + value);
                            echCount++;
                        }
                        else {
                            log.info("그냥 데이터 : " + value);
                        }

                        if (i == groupValues.size() - 1) {
                            searchQuery.append("'").append(value).append("');");
                        } else {
                            searchQuery.append("'").append(value).append("', ");
                        }
                    }

                    log.info("인서트 쿼리 : "+searchQuery);

                    // 실행할 쿼리문담기
                    queryList.add(String.valueOf(searchQuery));
                }
            }

            log.info("실행할 쿼리문 리스트 : "+queryList);
            for(String query : queryList) {
                dynamicUserRepositoryCustom.userCommonTable(query);
            }

            // 개인정보 생성로그 저장
            privacyHistoryService.privacyHistoryInsert(adminId, PrivacyHistoryCode.PHC_01, 2, kphReason, CommonUtil.publicIp(), email);

            // 암호화 횟수 저장
            if(echCount > 0) {
                encrypCountHistoryService.encrypCountHistorySave(cpCode, echCount);
            }

            // API 호출 로그 저장
            apiCallHistoryService.apiCallHistorySave(cpCode, "/v3/api/Auth/register");

            data.put("kokonut_IDX",kokonutIdx); // kokonut_IDX 리턴

        } else {
            log.error("파라미터 데이터가 없습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_00.getCode(),ResponseErrorCode.ERROR_CODE_00.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }












}
