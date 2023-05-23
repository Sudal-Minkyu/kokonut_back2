package com.app.kokonutapi.auth;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.company.companydatakey.CompanyDataKeyService;
import com.app.kokonutapi.auth.dtos.AuthApiLoginDto;
import com.app.kokonutuser.KokonutUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Woody
 * Date : 2023-04-27
 *
 * Time :
 * Remark :
 */
@Slf4j
@Service
public class AuthApiService {

    private final AdminRepository adminRepository;
    private final CompanyDataKeyService companyDataKeyService;
    private final PasswordEncoder passwordEncoder;

    private final KokonutUserService kokonutUserService;

    @Autowired
    public AuthApiService(AdminRepository adminRepository, CompanyDataKeyService companyDataKeyService, PasswordEncoder passwordEncoder, KokonutUserService kokonutUserService){
        this.adminRepository = adminRepository;
        this.companyDataKeyService = companyDataKeyService;
        this.passwordEncoder = passwordEncoder;
        this.kokonutUserService = kokonutUserService;
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
    public ResponseEntity<Map<String, Object>> apiRegister(HashMap<String, Object> paramMap, JwtFilterDto jwtFilterDto) throws Exception {
        log.info("apiRegister 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("paramMap : "+paramMap);

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        String cpCode = adminCompanyInfoDto.getCompanyCode();

//        AwsKmsResultDto awsKmsResultDto = companyDataKeyService.findByCompanyDataKey(cpCode);

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

//        paramMap = {14_2=test8, 15_1=test9, 1_pw=비번, 11_3=test7, 12_2=test5, 12_1=test4, 4_1=test6, 2_12=test2, 1_13=test3, 1_11=test1, 1_id=아이디}

        int trigger = 0;
        if(paramMap != null) {


            // 1번
//            Map<Integer, List<Map.Entry<String, Object>>> groupedMap = paramMap.entrySet().stream()
//                    .collect(Collectors.groupingBy(
//                            e -> Integer.parseInt(e.getKey().split("_")[0])
//                    ));
//
//            TreeMap<Integer, List<Map.Entry<String, Object>>> sortedGroupedMap = new TreeMap<>(groupedMap);
//            log.info("sortedGroupedMap: " + sortedGroupedMap);
//            sortedGroupedMap.forEach((k,v) -> {
//                log.info("Group: " + k);
//                v.forEach(entry -> log.info("  " + entry.getKey() + " = " + entry.getValue()));
//            });

            // 2번
            Map<Integer, List<Map.Entry<String, Object>>> groupedMap = new TreeMap<>();

            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                String[] parts = entry.getKey().split("_");
                if (parts.length != 2) throw new IllegalArgumentException("Invalid key: " + entry.getKey());

                try {
                    int numKey = Integer.parseInt(parts[0]);
                    String objKey = parts[1];

                    if (!objKey.equals("id") && !objKey.equals("pw") && !objKey.matches("-?\\d+(\\.\\d+)?")) {
                        throw new NumberFormatException(entry.getKey());
                    }

                    if (!groupedMap.containsKey(numKey)) {
                        groupedMap.put(numKey, new ArrayList<>());
                    }
                    groupedMap.get(numKey).add(entry);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(entry.getKey());
                }
            }
            log.info("groupedMap: " + groupedMap);


//            // 받은 파라메터값을 정렬한다.
//            Map<String, List<Map.Entry<String, Object>>> sortedGroupedMap = paramMap.entrySet().stream()
//                    .sorted((e1, e2) -> {
//                        String[] parts1 = e1.getKey().split("_");
//                        String[] parts2 = e2.getKey().split("_");
//
//                        String key1 = parts1[0];
//                        String key2 = parts2[0];
//                        String subKey1 = parts1[1];
//                        String subKey2 = parts2[1];
//
//                        if(key1.equals("id") || key1.equals("pw")){
//                            return key2.equals("id") || key2.equals("pw") ? key1.compareTo(key2) : -1;
//                        }
//                        if(key2.equals("id") || key2.equals("pw")){
//                            return 1;
//                        }
//
//                        try {
//                            Integer.parseInt(subKey1);
//                            Integer.parseInt(subKey2);
//                        } catch (NumberFormatException e) {
//                            if (!(subKey1.equals("id") || subKey1.equals("pw"))) {
//                                throw new IllegalArgumentException("Invalid key: " + e1.getKey(), e);
//                            }
//                            if (!(subKey2.equals("id") || subKey2.equals("pw"))) {
//                                throw new IllegalArgumentException("Invalid key: " + e2.getKey(), e);
//                            }
//                        }
//
//                        return Integer.compare(Integer.parseInt(key1), Integer.parseInt(key2));
//                    }).collect(Collectors.groupingBy(entry -> entry.getKey().split("_")[0], LinkedHashMap::new, Collectors.toList()))
//                    .entrySet().stream()
//                    .sorted((e1, e2) -> {
//                        String key1 = e1.getKey();
//                        String key2 = e2.getKey();
//
//                        if(key1.equals("id") || key1.equals("pw")){
//                            return key2.equals("id") || key2.equals("pw") ? key1.compareTo(key2) : -1;
//                        }
//                        if(key2.equals("id") || key2.equals("pw")){
//                            return 1;
//                        }
//
//                        try {
//                            return Integer.compare(Integer.parseInt(key1), Integer.parseInt(key2));
//                        } catch (NumberFormatException e) {
//                            if (!key1.equals("id") && !key1.equals("pw")) {
//                                throw new IllegalArgumentException("Invalid key: " + key1, e);
//                            } else {
//                                return 0;
//                            }
//                        }
//                    }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

//            log.info("sortedGroupedMap : " + sortedGroupedMap);

            List<String> queryList = new ArrayList<>();

            // 정렬된 값을 통해 회원가입을 한다.
//            for (Map.Entry<String, List<Map.Entry<String, Object>>> groupEntry : sortedGroupedMap.entrySet()) {
//                String groupKey = groupEntry.getKey();
//
//                // 회원가입저장하는데 맨앞 키가 "1"이 아닐 경우 에러를 던저준다.
//                // 내용 : 회원가입 할 경우에만 호출할 수 있는 테이블입니다. 회원가입에 필요한 필수사항을 살펴보신 후 호출하신 고유코드를 재확인 해주시길 바랍니다.
////                if(trigger == 0 && !groupKey.equals("1")) {
////                    return null;
////                }else {
////                    trigger++;
////                }
//
//
//                String saveTable = cpCode+"_"+groupKey;
//                log.info("그룹키 : " + groupKey);
//                log.info("컬럼 조회할 테이블 : "+saveTable);
//
//                List<Map.Entry<String, Object>> groupValues = groupEntry.getValue();
//                log.info("그룹키의 벨류값 : " + groupValues);
//                if(groupKey.equals("1")) {
//                    // 필수항목 검증 : 필수사항의 항목은 "1_id" 와 "1_pw" 입니다.
//                    for (Map.Entry<String, Object> entry : groupValues) {
//                        if(String.valueOf(entry.getKey()).equals("1_id") || String.valueOf(entry.getKey()).equals("1_pw")) {
//                            trigger++;
//                        }
//                    }
//
//                    // trigger 이 "2"가 아닐경우 에러를 던저준다.
//                    if(trigger != 2) {
//                        // 내용 : 필수항목을 넣지 않거나, 중복전송을 하셨습니다. "1_id"와"1_pw"는 하나씩만 보내주시길 바랍니다.
//                        return ResponseEntity.ok(res.success(data));
//                    } else {
//                        log.info("trigger : " + trigger);
//                    }
//                }
//
//                // 존재하는 테이블인지 검증하는 절차
//
//                // cpCode와 해당 그룰키를 통해 해당 테이블의 컬럼값을 조회한다.
//                int verifiResult= kokonutUserService.getTableVerification(saveTable);
//                log.info("verifiResult : "+verifiResult);
//
//                if(verifiResult == 0) {
//                    // 에러처리 : 존재하지 않은 테이블입니다. 존재하는 고유코드인지 확인해주세요.
//                    return ResponseEntity.ok(res.success(data));
//                } else {
//                    // 인서트를 시작한다.
//                    log.info("인서트 시작");
//
//                }
////                for (Map.Entry<String, Object> entry : groupValues) {
////                    String key = entry.getKey();
////                    Object value = entry.getValue();
////
////                    log.info("key : "+key);
////                    log.info("value : "+value);
////                }
//            }

            log.info("실행할 쿼리문 리스트 : "+queryList);

//            sortedGroupedMap.forEach((key, value) -> {
//                log.info("key: " + key);
//                log.info("value: " + value);
////                value.forEach(entry ->  log.info("  Key: " + entry.getKey() + ", Value: " + entry.getValue()));
//            });
        }

        return ResponseEntity.ok(res.success(data));
    }




}
