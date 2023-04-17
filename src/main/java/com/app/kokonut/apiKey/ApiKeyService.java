package com.app.kokonut.apiKey;

import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dto.ActivityCode;
import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.admin.dtos.AdminOtpKeyDto;
import com.app.kokonut.apiKey.dtos.ApiKeyAccessIpDto;
import com.app.kokonut.apiKey.dtos.ApiKeyDto;
import com.app.kokonut.apiKey.dtos.ApiKeyInfoDto;
import com.app.kokonut.apiKey.dtos.ApiKeyIpDeleteDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.configs.GoogleOTP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.springframework.transaction.annotation.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Woody
 * Date : 2022-10-25
 * Remark :
 */
@Slf4j
@Service
public class ApiKeyService {

    private final GoogleOTP googleOTP;
    private final HistoryService historyService;

    private final ApiKeyRepository apiKeyRepository;
    private final AdminRepository adminRepository;

    public ApiKeyService(GoogleOTP googleOTP, HistoryService historyService,
                         ApiKeyRepository apiKeyRepository, AdminRepository adminRepository) {
        this.googleOTP = googleOTP;
        this.historyService = historyService;
        this.apiKeyRepository = apiKeyRepository;
        this.adminRepository = adminRepository;
    }

    // API Key 생성 함수 => AES 방식
    public static String keyGenerate(final int keyLen) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(keyLen);
        SecretKey secretKey = keyGen.generateKey();
        byte[] encoded = secretKey.getEncoded();
        return DatatypeConverter.printHexBinary(encoded).toLowerCase();
    }

    // API Key 중복체크 함수
    public String checkApiKey(String akKey) throws NoSuchAlgorithmException {
        boolean keyCheck = apiKeyRepository.existsByAkKey(akKey);
        log.info("keyCheck : "+keyCheck);
        if(keyCheck) {
            akKey =  keyGenerate(128);
            checkApiKey(akKey);
        }
//        log.info("akKey : "+akKey);
        return akKey;
    }

    // 유저가 API Key를 가지고 있는지 체크하는 함수
    public ResponseEntity<Map<String, Object>> apiKeyCheck(String knEmail) {
        log.info("apiKeyCheck 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(knEmail);

        if(adminCompanyInfoDto != null) {
            ApiKeyDto apiKeyDto = apiKeyRepository.findByApiKey(adminCompanyInfoDto.getAdminId(), adminCompanyInfoDto.getCompanyId());
//            log.info("apiKeyDto : "+apiKeyDto);
            if(apiKeyDto == null) {
                data.put("result", 1);
            } else {
                ApiKeyAccessIpDto apiKeyAccessIpDto;
                List<ApiKeyAccessIpDto> accessIpList = new ArrayList<>();
                if(apiKeyDto.getAkAgreeIp1() != null) {
                    apiKeyAccessIpDto = new ApiKeyAccessIpDto();
                    apiKeyAccessIpDto.setAccessIp(apiKeyDto.getAkAgreeIp1());
                    apiKeyAccessIpDto.setMemo(apiKeyDto.getAkAgreeMemo1());
                    accessIpList.add(apiKeyAccessIpDto);
                }
                if(apiKeyDto.getAkAgreeIp2() != null) {
                    apiKeyAccessIpDto = new ApiKeyAccessIpDto();
                    apiKeyAccessIpDto.setAccessIp(apiKeyDto.getAkAgreeIp2());
                    apiKeyAccessIpDto.setMemo(apiKeyDto.getAkAgreeMemo2());
                    accessIpList.add(apiKeyAccessIpDto);
                }
                if(apiKeyDto.getAkAgreeIp3() != null) {
                    apiKeyAccessIpDto = new ApiKeyAccessIpDto();
                    apiKeyAccessIpDto.setAccessIp(apiKeyDto.getAkAgreeIp3());
                    apiKeyAccessIpDto.setMemo(apiKeyDto.getAkAgreeMemo3());
                    accessIpList.add(apiKeyAccessIpDto);
                }
                if(apiKeyDto.getAkAgreeIp4() != null) {
                    apiKeyAccessIpDto = new ApiKeyAccessIpDto();
                    apiKeyAccessIpDto.setAccessIp(apiKeyDto.getAkAgreeIp4());
                    apiKeyAccessIpDto.setMemo(apiKeyDto.getAkAgreeMemo4());
                    accessIpList.add(apiKeyAccessIpDto);
                }
                if(apiKeyDto.getAkAgreeIp5() != null) {
                    apiKeyAccessIpDto = new ApiKeyAccessIpDto();
                    apiKeyAccessIpDto.setAccessIp(apiKeyDto.getAkAgreeIp5());
                    apiKeyAccessIpDto.setMemo(apiKeyDto.getAkAgreeMemo5());
                    accessIpList.add(apiKeyAccessIpDto);
                }
                String akKey = apiKeyDto.getAkKey();
                StringBuilder filterApiKey = new StringBuilder(akKey.substring(0, 5));
                filterApiKey.append("*".repeat(Math.max(0, akKey.length() - 10)));
                filterApiKey.append(akKey, akKey.length()-5, akKey.length());

                data.put("result", 2);
                data.put("filterApiKey", filterApiKey);
                data.put("apiKey", akKey);
                data.put("accessIpList", accessIpList);
            }
        } else {
            log.error("해당 유저가 존재하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(),"해당 유저가 "+ResponseErrorCode.KO004.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // ApiKey 발급 및 재발급
    @Transactional
    public ResponseEntity<Map<String, Object>> apiKeyIssue(JwtFilterDto jwtFilterDto) throws NoSuchAlgorithmException {
        log.info("apiKeyIssue 호출");

        log.info("jwtFilterDto : "+jwtFilterDto);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        Long adminId = adminCompanyInfoDto.getAdminId();
        Long companyId = adminCompanyInfoDto.getCompanyId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();

        // 활동코드
        ActivityCode activityCode;
        String ip = CommonUtil.clientIp();
        Long activityHistoryId;

        Optional<ApiKey> optionalApiKey = apiKeyRepository.findApiKeyByAdminIdAndCompanyId(adminId, companyId);
        if(optionalApiKey.isPresent()) {
//            log.info("현재 API Key가 존재한다.");
//            AC_24("AC_24", "API KEY 발급"),
//            AC_25("AC_25", "API KEY 재발급"),

            // API KEY 재발급 코드
            activityCode = ActivityCode.AC_25;

            // 활동이력 저장 -> 비정상 모드
            activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, jwtFilterDto.getEmail());

            String akKey = optionalApiKey.get().getAkKey();
            log.info("현재 API Key : "+akKey);

            // 키 생성
            akKey = keyGenerate(128);
            log.info("재발급된 API Key : "+akKey);

            // 중복 여부 확인
            akKey = checkApiKey(akKey);

            optionalApiKey.get().setAkKey(akKey);
            optionalApiKey.get().setModify_email(jwtFilterDto.getEmail());
            optionalApiKey.get().setModify_date(LocalDateTime.now());
            apiKeyRepository.save(optionalApiKey.get());
        } else {
//            log.info("현재 API Key가 존재하지 않는다.");

            // API KEY 발급 코드
            activityCode = ActivityCode.AC_24;

            // 활동이력 저장 -> 비정상 모드
            activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, jwtFilterDto.getEmail());

            ApiKey apiKey = new ApiKey();

            // 키 생성
            String akKey = keyGenerate(128);
            log.info("발급된 API Key : "+akKey);

            // 중복 여부 확인
            akKey = checkApiKey(akKey);
            apiKey.setAdminId(adminId);
            apiKey.setCompanyId(companyId);
            apiKey.setAkUseYn("Y");
            apiKey.setAkKey(akKey);
            apiKey.setInsert_email(jwtFilterDto.getEmail());
            apiKey.setInsert_date(LocalDateTime.now());
            apiKeyRepository.save(apiKey);
        }

        historyService.updateHistory(activityHistoryId,
                companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);

        return ResponseEntity.ok(res.success(data));
    }

    // APIKey 허용 IP 등록
    @Transactional
    public ResponseEntity<Map<String, Object>> apiKeyIpSave(String accessIp, String ipMemo, JwtFilterDto jwtFilterDto) {
        log.info("apiKeyIpSave 호출");

        log.info("accessIp : "+accessIp);
        log.info("ipMemo : "+ipMemo);
        log.info("email : "+jwtFilterDto.getEmail());

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        Long adminId = adminCompanyInfoDto.getAdminId();
        Long companyId = adminCompanyInfoDto.getCompanyId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();

        // API KEY 허용 IP등록 코드
        ActivityCode activityCode = ActivityCode.AC_33;
        String ip = CommonUtil.clientIp();

        Optional<ApiKey> optionalApiKey = apiKeyRepository.findApiKeyByAdminIdAndCompanyId(adminId, companyId);
        if(optionalApiKey.isPresent()) {

            // 활동이력 저장 -> 비정상 모드
            Long activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, jwtFilterDto.getEmail());

            while(true) {
                if(optionalApiKey.get().getAkAgreeIp1() == null) {
                    optionalApiKey.get().setAkAgreeIp1(accessIp);
                    optionalApiKey.get().setAkAgreeMemo1(ipMemo);
                    break;
                }
                if(optionalApiKey.get().getAkAgreeIp2() == null) {
                    optionalApiKey.get().setAkAgreeIp2(accessIp);
                    optionalApiKey.get().setAkAgreeMemo2(ipMemo);
                    break;
                }
                if(optionalApiKey.get().getAkAgreeIp3() == null) {
                    optionalApiKey.get().setAkAgreeIp3(accessIp);
                    optionalApiKey.get().setAkAgreeMemo3(ipMemo);
                    break;
                }
                if(optionalApiKey.get().getAkAgreeIp4() == null) {
                    optionalApiKey.get().setAkAgreeIp4(accessIp);
                    optionalApiKey.get().setAkAgreeMemo4(ipMemo);
                    break;
                }
                if(optionalApiKey.get().getAkAgreeIp5() == null) {
                    optionalApiKey.get().setAkAgreeIp5(accessIp);
                    optionalApiKey.get().setAkAgreeMemo5(ipMemo);
                    break;
                }

                log.error("등록되신 API Key의 허용IP 수가 초과되었습니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO081.getCode(),ResponseErrorCode.KO081.getDesc()));
            }

            optionalApiKey.get().setModify_email(jwtFilterDto.getEmail());
            optionalApiKey.get().setModify_date(LocalDateTime.now());
            apiKeyRepository.save(optionalApiKey.get());

            historyService.updateHistory(activityHistoryId,
                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);

        } else {
            log.error("등록되신 API Key가 존재하지 않습니다. API Key 먼저 발급해주시길 바랍니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO080.getCode(),ResponseErrorCode.KO080.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // APIKey 허용 IP 삭제
    @Transactional
    public ResponseEntity<Map<String, Object>> apiKeyIpDelete(ApiKeyIpDeleteDto apiKeyIpDeleteDto, JwtFilterDto jwtFilterDto) {
        log.info("apiKeyIpDelete 호출");

        String otpValue = apiKeyIpDeleteDto.getOtpValue();
        List<String> deleteIpList = apiKeyIpDeleteDto.getDeleteIpList();

        log.info("otpValue : "+otpValue);
        log.info("deleteIpList : "+deleteIpList);
        log.info("email : "+jwtFilterDto.getEmail());

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        if(otpValue == null || otpValue.equals("")) {
            log.error("구글 OTP 값이 존재하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO010.getCode(),ResponseErrorCode.KO010.getDesc()));
        }

        // 입력한 구글 OTP 값 검증
        AdminOtpKeyDto adminOtpKeyDto = adminRepository.findByOtpKey(jwtFilterDto.getEmail());
        if(adminOtpKeyDto != null) {
            // OTP 검증 절차
            boolean auth = googleOTP.checkCode(otpValue, adminOtpKeyDto.getKnOtpKey());

            if (!auth) {
                log.error("입력된 구글 OTP 값이 일치하지 않습니다. 확인해주세요.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO012.getCode(), ResponseErrorCode.KO012.getDesc()));
            }
        } else {
            log.error("등록된 OTP가 존재하지 않습니다. 구글 OTP 2단계 인증을 등록해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO011.getCode(), ResponseErrorCode.KO011.getDesc()));
        }

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        Long adminId = adminCompanyInfoDto.getAdminId();
        Long companyId = adminCompanyInfoDto.getCompanyId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();

        // API KEY 허용 IP등록 코드
        ActivityCode activityCode = ActivityCode.AC_34;
        String ip = CommonUtil.clientIp();

        Optional<ApiKey> optionalApiKey = apiKeyRepository.findApiKeyByAdminIdAndCompanyId(adminId, companyId);
        if(optionalApiKey.isPresent()) {

            // 활동이력 저장 -> 비정상 모드
            Long activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, jwtFilterDto.getEmail());

            for (String deleteIp : deleteIpList) {
                while (true) {
                    if (optionalApiKey.get().getAkAgreeIp1() != null && optionalApiKey.get().getAkAgreeIp1().equals(deleteIp)) {
                        optionalApiKey.get().setAkAgreeIp1(null);
                        optionalApiKey.get().setAkAgreeMemo1(null);
                        break;
                    }
                    if (optionalApiKey.get().getAkAgreeIp2() != null && optionalApiKey.get().getAkAgreeIp2().equals(deleteIp)) {
                        optionalApiKey.get().setAkAgreeIp2(null);
                        optionalApiKey.get().setAkAgreeMemo2(null);
                        break;
                    }
                    if (optionalApiKey.get().getAkAgreeIp3() != null && optionalApiKey.get().getAkAgreeIp3().equals(deleteIp)) {
                        optionalApiKey.get().setAkAgreeIp3(null);
                        optionalApiKey.get().setAkAgreeMemo3(null);
                        break;
                    }
                    if (optionalApiKey.get().getAkAgreeIp4() != null && optionalApiKey.get().getAkAgreeIp4().equals(deleteIp)) {
                        optionalApiKey.get().setAkAgreeIp4(null);
                        optionalApiKey.get().setAkAgreeMemo4(null);
                        break;
                    }
                    if (optionalApiKey.get().getAkAgreeIp5() != null && optionalApiKey.get().getAkAgreeIp5().equals(deleteIp)) {
                        optionalApiKey.get().setAkAgreeIp5(null);
                        optionalApiKey.get().setAkAgreeMemo5(null);
                        break;
                    }

                    log.error("삭제하실 허용IP가 존재하지 않습니다.");
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO082.getCode(), ResponseErrorCode.KO082.getDesc()));
                }
            }


            optionalApiKey.get().setModify_email(jwtFilterDto.getEmail());
            optionalApiKey.get().setModify_date(LocalDateTime.now());
            apiKeyRepository.save(optionalApiKey.get());

            historyService.updateHistory(activityHistoryId,
                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);

        } else {
            log.error("등록되신 API Key가 존재하지 않습니다. API Key 먼저 발급해주시길 바랍니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO080.getCode(),ResponseErrorCode.KO080.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }


    // ApiKey가 존재하는지 그리고 유효한지 검증하는 메서드
    public Long validateApiKey(String apikey) {
        return apiKeyRepository.findByCheck(apikey);
    }

    public ApiKeyInfoDto findByApiKeyInfo(String apikey) {
        return apiKeyRepository.findByApiKeyInfo(apikey);
    }

    // 유저IP 체킹
    public Long findByApiKeyCheck(String userIp) {
        return apiKeyRepository.findByApiKeyCheck(userIp);
    }



//    /**
//     * Api Key Insert
//     */
////	public void InsertApiKey(HashMap<String, Object> paramMap) {
////		dao.InsertApiKey(paramMap);
////	}
//    /** JPA save()로 재구성 : insertApiKey -> 변경후
//     * 원래 받는 데이터 -> HashMap<String,Object> paramMap 형식
//     * Api Key Insert
//     * param
//     * - Long adminId, Long companyId, String registerName, Integer type, Integer state
//     * - String key
//     */
//    @Transactional
//    public Integer insertApiKey(Long adminId, Long companyId, String registerName, Integer type, Integer state,
//                             String key, Integer useAccumulate) {
//        log.info("insertApiKey 호출");
//
//        Date systemDate = new Date(System.currentTimeMillis());
//        log.info("현재 날짜 : "+systemDate);
//
//        ApiKey apiKey = new ApiKey();
//        apiKey.setadminId(adminId);
//        apiKey.setcompanyId(companyId);
//        apiKey.setRegisterName(registerName);
//        apiKey.setType(type);
//        apiKey.setState(state);
//        apiKey.setUseAccumulate(useAccumulate);
//        apiKey.setKey(key);
//        apiKey.setRegdate(LocalDateTime.now());
//        apiKey.setUseYn("Y");
//
//        if(type != null && type.equals(2)){
//            // 현재 LocalDate에서 14일후인 날짜계산
//            Calendar c = Calendar.getInstance();
//            c.setTime(systemDate);
//            c.add(Calendar.DATE, 14);
//
//            Date fourteenDayAfter = new Date(c.getTimeInMillis());
//            log.info("14일후 날짜 : "+fourteenDayAfter);
//
//            apiKey.setValidityStart(systemDate);
//            apiKey.setValidityEnd(fourteenDayAfter);
//        }
//
//        return apiKeyRepository.save(apiKey).getIdx();
//    }
//
//    /**
//     * Api Key Update
//     */
////	public void UpdateApiKey(HashMap<String, Object> paramMap) {
////		dao.UpdateApiKey(paramMap);
////	}
//    /** JPA save()로 재구성 : updateApiKey -> 변경후
//     * Api Key Update
//     * 원래 받는 데이터 -> HashMap<String,Object> paramMap 형식
//     * param
//     * - Integer idx, String useYn, String reason, Integer modifierIdx, String modifierName
//     */
//
//    @Transactional
//    public void updateApiKey(Integer idx, String useYn, String reason, Integer modifierIdx, String modifierName) {
//        log.info("updateApiKey 호출");
//
//        ApiKey apiKey = apiKeyRepository.findById(idx)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 'ApiKey' 입니다."));
//
//        Date systemDate = new Date(System.currentTimeMillis());
//        log.info("현재 날짜 : "+systemDate);
//
//        apiKey.setUseYn(useYn);
//        apiKey.setReason(reason);
//
//        apiKey.setModifierIdx(modifierIdx);
//        apiKey.setModifierName(modifierName);
//        apiKey.setModifyDate(LocalDateTime.now());
//
//        apiKeyRepository.save(apiKey);
//    }
//
//    /**
//     * Api Key 삭제
//     * @param idx
//     */
////	public void DeleteApiKeyByIdx(int idx) {
////		dao.DeleteApiKeyByIdx(idx);
////	}
//    /** JPA delete()로 재구성 : deleteApiKeyByIdx -> 변경후
//     * Api Key 삭제
//     * param
//     * - Integer idx
//     */
//    @Transactional
//    public void deleteApiKeyByIdx(Integer idx) {
//        log.info("deleteApiKeyByIdx 호출");
//
//        ApiKey apiKey = apiKeyRepository.findById(idx)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 'ApiKey' 입니다."));
//
//        apiKeyRepository.delete(apiKey);
//    }
//
//	/**
//	 * Api Key 리스트
//	 */
////	public List<HashMap<String, Object>> SelectApiKeyList(HashMap<String, Object> paramMap) {
////		return dao.SelectApiKeyList(paramMap);
////	}
//    public List<ApiKeyListAndDetailDto> findByApiKeyList(ApiKeyMapperDto apiKeyMapperDto) {
//        log.info("findByApiKeyList 호출");
//        return apiKeyRepository.findByApiKeyList(apiKeyMapperDto);
//    }
//
//	/**
//	 * Api Key 리스트 Count
//	 */
////	public int SelectApiKeyListCount(HashMap<String, Object> paramMap) {
////        return dao.SelectApiKeyListCount(paramMap);
////    }
//    public Long findByApiKeyListCount(ApiKeyMapperDto apiKeyMapperDto) {
//        log.info("findByApiKeyListCount 호출");
//        return apiKeyRepository.findByApiKeyListCount(apiKeyMapperDto);
//    }
//
////	/**
////	 * Api Key 상세보기
////	 * @param idx
////	 */
////	public HashMap<String, Object> SelectApiKeyByIdx(int idx) {
////		return dao.SelectApiKeyByIdx(idx);
////	}
//    public ApiKeyListAndDetailDto findByApiKeyDetail(Integer idx) {
//        log.info("findByApiKeyDetail 호출");
//        return apiKeyRepository.findByApiKeyDetail(idx);
//    }
//
//    /**
//     * ApiKey 조회
//     * @param key
//     * @return
//     */
////    public HashMap<String, Object> SelectByKey(String key) {
////        return apiKeyRepository.SelectByKey(key);
////    }
//     public ApiKeyKeyDto findByKey(String key) {
//         log.info("findByKey 호출");
//         return apiKeyRepository.findByKey(key);
//     }
//
//	/**
//	 * Test Api Key 조회
//	 * @param companyId
//	 */
////	public HashMap<String, Object> SelectTestApiKeyBycompanyId(Long companyId) {
////		HashMap<String, Object> paramMap = new HashMap<String, Object>();
////		paramMap.put("companyId", companyId);
////
////		return dao.SelectTestApiKeyBycompanyId(paramMap);
////	}
//    public ApiKeyListAndDetailDto findByTestApiKeyBycompanyId(Long companyId) {
//        log.info("findByTestApiKeyBycompanyId 호출");
//        return apiKeyRepository.findByTestApiKeyBycompanyId(companyId, 2);
//    }
//
//	/**
//	 * Test Api Key 중복 조회
//	 * @param key
//	 */
////	public int SelectTestApiKeyDuplicateCount(String key) {
////		return dao.SelectTestApiKeyDuplicateCount(key);
////	}
//    public Long findByTestApiKeyDuplicateCount(String key) {
//        log.info("findByTestApiKeyDuplicateCount 호출");
//        return apiKeyRepository.findByTestApiKeyDuplicateCount(key, 2);
//    }
//
//	/**
//	 * 일반 Api Key 조회
//	 * @param companyId
//	 */
////	public HashMap<String, Object> SelectApiKeyBycompanyId(Long companyId) {
////		HashMap<String, Object> paramMap = new HashMap<String, Object>();
////		paramMap.put("companyId", companyId);
////
////		return dao.SelectApiKeyBycompanyId(paramMap);
////	}
//    public ApiKeyListAndDetailDto findByApiKeyBycompanyId(Long companyId) {
//        log.info("findByApiKeyBycompanyId 호출");
//        return apiKeyRepository.findByApiKeyBycompanyId(companyId, 1, "Y");
//    }
//
//	/**
//	 * Api Key 중복 조회
//	 * @param key
//	 */
////	public int SelectApiKeyDuplicateCount(String key) {
////		return dao.SelectApiKeyDuplicateCount(key);
////	}
//    public Long findByApiKeyDuplicateCount(String key) {
//        log.info("findByApiKeyDuplicateCount 호출");
//        return apiKeyRepository.findByApiKeyDuplicateCount(key, 1);
//    }
//
//	/**
//	 * 만료 예정인 Test API Key 리스트
//	 */
////	public List<HashMap<String, Object>> SelectTestApiKeyExpiredList(HashMap<String, Object> paramMap) {
////		return dao.SelectTestApiKeyExpiredList(paramMap);
////	}
//    public List<TestApiKeyExpiredListDto> findByTestApiKeyExpiredList(HashMap<String, Object> paramMap) {
//        log.info("findByTestApiKeyExpiredList 호출");
//        return apiKeyRepository.findByTestApiKeyExpiredList(paramMap, 2);
//    }
//
//    // 검토필요
//	public static String keyGenerate(final int keyLen) throws NoSuchAlgorithmException {
//        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//        keyGen.init(keyLen);
//        SecretKey secretKey = keyGen.generateKey();
//        byte[] encoded = secretKey.getEncoded();
//        return DatatypeConverter.printHexBinary(encoded).toLowerCase();
//    }
//
//
//	/**
//	 * api key block 처리 - 결제 취소 시 사용
//	 */
////	public void UpdateBlockKey(Long companyId) {
////		dao.UpdateBlockKey(companyId);
////	}
//    /** JPA save()로 재구성 : updateBlockKey -> 변경후
//     * 결제 취소 시 사용
//     * param
//     * - Long companyId
//     */
//    @Transactional
//    public void updateBlockKey(Long companyId) {
//        log.info("updateBlockKey 호출");
//
//        ApiKey apiKey = apiKeyRepository.findApiKeyBycompanyIdAndType(companyId,1)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 'ApiKey' 입니다."));
//        apiKey.setUseYn("N");
//
//        apiKeyRepository.save(apiKey);
//    }
//
//	/*
//	 * 사용중인 TEST API KEY가 존재한다면 만료처리
//	 */
////	public void UpdateTestKeyExpire(Long companyId) {
////		dao.UpdateTestKeyExpire(companyId);
////	}
//    /** JPA save()로 재구성 : updateTestKeyExpire -> 변경후
//     * 사용중인 TEST API KEY가 존재한다면 만료처리
//     * param
//     * - Long companyId
//     */
//    @Transactional
//    public void updateTestKeyExpire(Long companyId) {
//        log.info("updateTestKeyExpire 호출");
//        Date systemDate = new Date(System.currentTimeMillis());
//
//        ApiKey apiKey = apiKeyRepository.findApiKeyBycompanyIdAndTypeDate(companyId,2, systemDate)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 'ApiKey' 입니다."));
//        apiKey.setValidityEnd(systemDate);
//
//        apiKeyRepository.save(apiKey);
//    }
//
//    /** JPA delete()로 재구성 : TotalDeleteService deleteApiKeyBycompanyId -> 변경후
//     * param
//     * - Long companyId
//     */
//    @Transactional
//    public void deleteApiKeyBycompanyId(Long companyId) {
//        log.info("deleteApiKeyBycompanyId 호출");
//
//        ApiKey apiKey = apiKeyRepository.findApiKeyBycompanyId(companyId)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 'ApiKey' 입니다."));
//
//        apiKeyRepository.delete(apiKey);
//    }
//
//
//    /**
//	 * API KEY BLOCK, Send MAIL
////	  @param companyId - 회사IDX
//	 */
////	public void BlockApiBycompanyId(Long companyId) {
////		HashMap<String, Object> apiMap = new HashMap<String, Object>();
////		apiMap.put("companyId", companyId);
////		apiMap.put("useYn", "Y");
////		List<HashMap<String, Object>> apiKeyList = SelectApiKeyList(apiMap);
////		for(HashMap<String, Object> apiKey : apiKeyList) {
////			final String LOG_HEADER = "[kokonut Api Block]";
////			dblogger.save(DBLogger.LEVEL.INFO, Integer.parseInt(apiKey.get("IDX").toString()), CommonUtil.clientIp(), DBLogger.TYPE.DELETE, LOG_HEADER, "delete");
////		}
////		UpdateBlockKey(companyId);
////	}
//    // 해당 메서드는 dblogger 유틸리티 추가하고 진행함
//
//    // TODO apiKey 구성이 변경 될 수 있어서 (테스트키의 유무, 유효기한의 유뮤, 삭제여부 등등) 구성 후 로직 작성
//    public ResponseEntity<Map<String,Object>> apiKeyManagement(String email, String userRole) {
//        log.info("apiKeyManagement 호출");
//        if(email == null){
//            log.error("email을 값을 확인 할 수 없습니다.");
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO067.getCode(), ResponseErrorCode.KO067.getDesc()));
//        }else{
//            // 해당 이메일을 통해 회사 IDX 조회
//            AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
//            if(adminCompanyInfoDto == null){
//                log.error("회사 정보를 조회할 수 없습니다. email : " + email);
//                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO002.getCode(), "회사 정보를 "+ResponseErrorCode.KO002.getDesc()));
//            }else{
//                Long companyId = adminCompanyInfoDto.getCompanyId();
//                boolean serviceValid = false;
//                /**
//                 * 기존 코코넛 서비스의 경우 로그인 할 때 apiKey를 함께 조회해서 kokonutUser에 다 담은 뒤
//                 * 해당 내용으로 검증함.
//                 **/
//                // apiKey 검사 0. apiKey 테이블에 해당 회사 정보가 없을 경우.
//                // apiKey 검사 1. 테스트 사용자 인가, 일반 사용자인가.
//                // apiKey 검사 2. 테스트 사용자 일 경우 기간 내의 사용자인가.
//
////                ApiKeyListAndDetailDto apiKeyListAndDetailDto = new ApiKeyListAndDetailDto();
////                switch (apiKeykeyDto.getType()) {
////                    case 1 :
////                        // 일반
////                        apiKeyListAndDetailDto = findByApiKeyBycompanyId(companyId);
////                        data.put("email", email);
////                        break;
////                    case 2 :
////                        // 테스트
////                        apiKeyListAndDetailDto = findByTestApiKeyBycompanyId(companyId);
////                        break;
////                }
////                data.put("apiKeyListAndDetailDto", apiKeyListAndDetailDto.getKey());
//
//                return ResponseEntity.ok(res.success(data));
//            }
//        }
//    }
//
//    // TODO apiKey 구성이 변경 될 수 있어서 (테스트키의 유무, 유효기한의 유뮤, 삭제여부 등등) 구성 후 로직 작성
//    public ResponseEntity<Map<String, Object>> testIssue(String email, String userRole) {
//        return null;
//    }
//
//    /**
//     * API KEY 발급
//     * @param email - 접속자 이메일
//     * @param userRole - 접속자 권한
//     */
//    public ResponseEntity<Map<String, Object>> issue(String email, String userRole) throws NoSuchAlgorithmException {
//        log.info("issue 호출");
//        if(email == null){
//            log.error("email 값을 확인 할 수 없습니다.");
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO067.getCode(), ResponseErrorCode.KO067.getDesc()));
//        }else{
//            // 이메일을 통해 계정 정보 가져오기.
//            Admin admin = adminRepository.findByKnEmail(email)
//                    .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : " + email));
//            Long adminId = admin.getAdminId();
//            Long companyId = admin.getCompanyId();
//            String userName = admin.getKnName();
//
//            // 기존 등록 키가 있는지 확인.
//            ApiKeyListAndDetailDto apiKeyListAndDetailDto = findByApiKeyBycompanyId(companyId);
//            if(apiKeyListAndDetailDto != null) {
//                // TODO errorCode 등록
//                log.error("이미 발급된 API Key가 존재합니다. 재발급을 진행해주세요.");
//                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO067.getCode(), ResponseErrorCode.KO067.getDesc()));
//            }else {
//                // 키 생성
//                String key = keyGenerate(128);
//                // 중복 여부 확인
//                int keyCount = findByApiKeyDuplicateCount(key).intValue();
//                while(true) {
//                    if(keyCount < 1) {
//                        // 중복이 아니면 그대로 생성된 키를 사용.
//                        break;
//                    } else {
//                        // 중복이면 키를 새로 생성.
//                        key =  keyGenerate(128);
//                    }
//                }
//                insertApiKey(adminId, companyId, userName, 1, 1, key, 1);
//                // TODO Activity History 활동 이력 남기기 - apk Key 발급
//
//                return ResponseEntity.ok(res.success(data));
//            }
//        }
//    }
//
//    /**
//     * API KEY 재발급
//     * @param email - 접속자 이메일
//     * @param userRole - 접속자 권한
//     */
//    public ResponseEntity<Map<String, Object>> reIssue(String email, String userRole) throws NoSuchAlgorithmException {
//        log.info("reIssue 호출");
//        if(email == null){
//            log.error("email 값을 확인 할 수 없습니다.");
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO067.getCode(), ResponseErrorCode.KO067.getDesc()));
//        }else{
//            if("test@kokonut.me".equals(email)){
//                log.error("체험하기모드는 할 수 없습니다.");
//                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO000.getCode(), ResponseErrorCode.KO000.getDesc()));
//            }else{
//                // 이메일을 통해 계정 정보 가져오기.
//                Admin admin = adminRepository.findByKnEmail(email)
//                        .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : " + email));
//                Long adminId = admin.getAdminId();
//                Long companyId = admin.getCompanyId();
//                String userName = admin.getKnName();
//
//                // 키 생성
//                String key = keyGenerate(128);
//                // 중복 여부 확인
//                int keyCount = findByApiKeyDuplicateCount(key).intValue();
//                while(true) {
//                    if(keyCount < 1) {
//                        // 중복이 아니면 그대로 생성된 키를 사용.
//                        break;
//                    } else {
//                        // 중복이면 키를 새로 생성.
//                        key =  keyGenerate(128);
//                    }
//                }
//                // TODO apiKeyMapperDto 수정 (companyId 추가)에 대해서 woody에게 물어보기
//                ApiKeyMapperDto apiKeyMapperDto = new ApiKeyMapperDto();
//                apiKeyMapperDto.setUseYn("Y");
//                //apiKeyMapperDto.setcompanyId(companyId);
//                List<ApiKeyListAndDetailDto> apiKeys = findByApiKeyList(apiKeyMapperDto);
//                for(ApiKeyListAndDetailDto apiKey : apiKeys) {
//                    // TODO Activity History 활동 이력 남기기. - api Key block 처리
//                }
//                // API KEY BLOCK 처리
//                updateBlockKey(companyId);
//                insertApiKey(adminId, companyId, userName, 1, 2, key, 1);
//                // TODO Activity History 활동 이력 남기기 - api key 재발급
//                return ResponseEntity.ok(res.success(data));
//            }
//        }
//    }
//
//    /**
//     * API KEY 수정 (블락, 언블락)
//     * @param idx - 수정할 api Key Idx
//     * @param useYn - Y,N 사용여부
//     * @param reason - 변경 사유
//     * @param email - 접속자 이메일
//     * @param userRole - 접속자 권한
//     */
//    public ResponseEntity<Map<String, Object>> modify(Integer idx, String useYn, String reason, String email, String userRole) {
//        log.info("modify 호출");
//        if(idx == null) {
//            log.error("idx 값을 확인 할 수 없습니다.");
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO051.getCode(), ResponseErrorCode.KO051.getDesc()));
//        }else if(email ==  null) {
//            log.error("email 값을 확인 할 수 없습니다.");
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO067.getCode(), ResponseErrorCode.KO067.getDesc()));
//        }else{
//            // 이메일을 통해 계정 정보 가져오기.
//            Admin admin = adminRepository.findByKnEmail(email)
//                    .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : " + email));
//            int modifierIdx = admin.getAdminId();
//            String userName = admin.getKnName();
//
//            updateApiKey(idx, useYn, reason, modifierIdx, userName);
//            // TODO activity History 남기기 - apkKey 블락, 언블락
//
//            return ResponseEntity.ok(res.success(data));
//        }
//    }
}
