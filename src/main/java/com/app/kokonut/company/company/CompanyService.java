package com.app.kokonut.company.company;

import com.app.kokonut.awsKmsHistory.AwsKmsHistoryRepository;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.realcomponent.AwsKmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Woody
 * Date : 2022-12-27
 * Time :
 * Remark :
 */
@Slf4j
@Service
public class CompanyService {

    private final AwsKmsUtil awsKmsUtil;

    private final CompanyRepository companyRepository;
    private final AwsKmsHistoryRepository awsKmsHistoryRepository;
    @Autowired
    public CompanyService(AwsKmsUtil awsKmsUtil, CompanyRepository companyRepository, AwsKmsHistoryRepository awsKmsHistoryRepository){
        this.awsKmsUtil = awsKmsUtil;
        this.companyRepository = companyRepository;
        this.awsKmsHistoryRepository = awsKmsHistoryRepository;
    }


    public ResponseEntity<Map<String,Object>> findByCompanyDetail(int idx) {

        log.info("findByCompanyDetail 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        data.put("", "");

        return ResponseEntity.ok(res.success(data));
    }


//    /**
//     * 회사정보 insert -> jpa save 활용
//     */
//    public void InsertCompany(HashMap<String, Object> paramMap) {
//        dao.InsertCompany(paramMap);
//    }

//    /**
//     * 회사정보 update -> jpa save 활용
//     */
//    public void UpdateCompany(HashMap<String, Object> paramMap) {
//        dao.UpdateCompany(paramMap);
//    }

//    /**
//     * 회사정보 등록자 update -> jpa save 활용
//     */
//    public void UpdateadminIdOfCompany(HashMap<String, Object> paramMap) {
//        dao.UpdateadminIdOfCompany(paramMap);
//    }

    /**
     * 회자 사업자 중복검사 체크
     */
//    SelectCompanyCountByBusinessNumber -> "existsByCpBusinessNumber"로 대체

    /**
     * 회사정보 상세보기
     * @param idx
     */
//    SelectCompanyByIdx -> "findById"로 대체

    /**
     * 회사정보 상세보기 -> 받아오는 데이터 폭이 너무 넓음 : 필요할때 마다 유동적으로 추가하기
     * @param idx
     */
//    public HashMap<String, Object> SelectCompanyByIdx(int idx) {
//        return dao.SelectCompanyByIdx(idx);
//    }

    /**
     * 회사정보 리스트 -> 받아오는 데이터 폭이 너무 넓음 : 필요할때 마다 유동적으로 추가하기
     */
//    public List<HashMap<String, Object>> SelectCompanyList(HashMap<String, Object> paramMap) {
//        return dao.SelectCompanyList(paramMap);
//    }

    /**
     * 회사 암호화 키 조회 -> 암호화된 키를 복호화한다.
     * @return DATA_KEY(복호화)
     * 최종적으로 복호화한 DATA_KEY를 전달하는 메서드
     */
    @Transactional
    public SecretKey selectCompanyEncryptKey(Long companyId) {

//        Optional<Company> optionalCompany = companyRepository.findById(companyId);
//
//        if(optionalCompany.isPresent()){
//            if(optionalCompany.get().getCpDataKey() == null) {
//                log.error("해당 기업의 dataKey 데이터가 존재하지 않습니다.");
//                return null;
//            }
//
//            String dataKey = optionalCompany.get().getCpDataKey();
//            AwsKmsResultDto awsKmsResultDto = awsKmsUtil.dataKeyDecrypt(dataKey);
//
//            if(awsKmsResultDto.getResult().equals("success")) {
//                log.info("KMS 암복호화 성공");
//
//                /* 복호화 후 이력저장 */
//                log.info("KMS 복호화 이력 저장 로직 시작");
//                AwsKmsHistory awsKmsHistory = new AwsKmsHistory();
//                awsKmsHistory.setAkhType("DEC");
//                awsKmsHistory.setAkhRegdate(LocalDateTime.now());
//                AwsKmsHistory saveAwsKmsHistory =  awsKmsHistoryRepository.save(awsKmsHistory);
//                log.info("KMS 복호화 이력 저장 saveAwsKmsHistory : "+saveAwsKmsHistory.getAkhIdx());
//
//                return awsKmsResultDto.getSecretKey();
//            }
//            else {
//                log.error("KMS 암복호화 실패");
//                return null;
//            }
//        }
//        else {
//            log.error("해당 기업은 존재하지 않습니다. companyId : "+companyId);
            return null;
//        }
    }

    /**
     * 마스터 계정 승인 시 _user 스키마에 테이블 생성과 동시에
     * KMS를 통해 암/복호화에 사용할 키를 생성한다.
     * -> 기존 코코넛에서도 사용하지 않고 있는 메서드
     */
//    public void UpdateEncryptOfCompany(HashMap<String, Object> paramMap) {
//        dao.UpdateEncryptOfCompany(paramMap);
//    }


    /**
     * Kokonut 암호화 키 조회
     * @return 암호화 키
     * @throws Exception
     * -> 기존 코코넛에서도 사용하지 않고 있는 메서드
     */
//    public String SelectKokonutEncryptKey() throws Exception {
//        HashMap<String, Object> data = SelectCompanyByName("contactKokonut");
//
//        if(data == null) {
//            throw new Exception("not found company info.");
//        }
//
//        if(!data.containsKey("ENCRYPT_TEXT")) {
//            throw new Exception("not found company encrypt text");
//        }
//
//        if(!data.containsKey("DATA_KEY")) {
//            throw new Exception("not found company data key");
//        }
//
//        String encrpyText = data.get("ENCRYPT_TEXT").toString();
//        String dataKey = data.get("DATA_KEY").toString();
//
//        /* 암호화키 복호화 처리*/
//        HashMap<String, Object> decryptedKeyInfo = awsKmsService.decrypt(encrpyText, dataKey);
//
//        /* 복호화 후 키 업데이트 처리 */
//        String decryptText = decryptedKeyInfo.get("decryptText").toString();
//        HashMap<String, Object> enc = awsKmsService.encrypt(decryptText);
//        if(enc.get("errorMsg").equals("")) {
//            enc.put("businessNumber", data.get("BUSINESS_NUMBER").toString());
//            dao.UpdateEncryptOfCompany(enc);
//        }
//
//        return decryptText;
//    }


    // 정기결제 정보 업데이트 -> jpa save 활용
//    public void UpdatePaymentInfo(HashMap<String, Object> paramMap) {
//        dao.UpdatePaymentInfo(paramMap);
//    }

    // 결제 정보 취소 처리 -> jpa save 활용
//    public void UpdatePaymentCancel(HashMap<String, Object> paramMap) {
//        dao.UpdatePaymentCancel(paramMap);
//    }

    // CUSTOM_UID 조회 -> 카드정보 저장을 하지 않기 떄문에 제외
//    public String SelectCustomUid(HashMap<String, Object> paramMap) {
//        return dao.SelectCustomUid(paramMap);
//    }

    // 자동 결제 후, 결제 정보 저장
//    public void UpdatePaymentAutoInfo(Long companyId, String validEnd, String payRequestUid) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
//
//        HashMap<String, Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("isAutoPay", 4);
//        paramMap.put("companyId", companyId);
//        Date today = new Date();
//        String validStart = sdf.format(today);
//        paramMap.put("validStart", validStart);
//
//        Date validEndDate = sdf.parse(validEnd);
//        Calendar tmpCal = Calendar.getInstance();
//        tmpCal = Calendar.getInstance();
//        tmpCal.setTime(validEndDate);
//        tmpCal.add(Calendar.MONTH, 1);
//        validEnd = sdf2.format(tmpCal.getTime())+"-05 23:59:59";
//        paramMap.put("validEnd", validEnd);
//
//        dao.UpdatePaymentAutoInfo(paramMap);
//    }

    // 회사정보 조회 -> findByCompanyName Repository 추가
//    public HashMap<String, Object> SelectCompanyByName(String companyName) {
//        return dao.SelectCompanyByName(companyName);
//    }

    /*
     * 누적 휴면 회원 값 업데이트. 휴면 계정으로 전환된 회원의 누적 값을 관리한다.
     */
//    public void UpdateDormantAccumulate(Long companyId, int dormantCount) {
//        HashMap<String, Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("companyId", companyId);
//        paramMap.put("dormantCount", dormantCount);
//        dao.UpdateDormantAccumulate(paramMap);
//    }
//
//    // 강제 해제
//    public void UpdateStopService(int amount, Long companyId) {
//        HashMap<String, Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("amount", amount);
//        paramMap.put("companyId", companyId);
//        dao.UpdateStopService(paramMap);
//    }

    // 인원초과 체크
//    public boolean CheckUserCount(Long companyId) {
//
//        HashMap<String, Object> company = SelectCompanyByIdx(companyId);
//        String businessNumber = company.get("BUSINESS_NUMBER").toString();
//        String isValid = company.get("IS_VALID").toString();
//
//        HashMap<String, Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("companyId", companyId);
//
//        HashMap<String, Object> testApiKey = apiKeydao.SelectTestApiKeyBycompanyId(paramMap);
//        String apiKeyValid = "";
//        String useYn = "";
//
//        if(testApiKey != null) {
//            apiKeyValid = testApiKey.get("IS_VALID").toString();
//            useYn = testApiKey.get("USE_YN").toString();
//
//            if("N".equals(isValid) && useYn.equals("N") && apiKeyValid.equals("N")) {
//                return false;
//            }
//        }
//
//        HashMap<String, Object> apiKey = apiKeydao.SelectApiKeyBycompanyId(paramMap);
//        if(apiKey != null) {
//            apiKeyValid = apiKey.get("IS_VALID").toString();
//            useYn = apiKey.get("USE_YN").toString();
//
//            String service = "";
//            if("N".equals(isValid) && useYn.equals("Y") && apiKeyValid.equals("Y")) {
//                service = "STANDARD";
//            } else {
//                service = company.get("SERVICE").toString();
//            }
//
//            if("BASIC".equals(service)) {
//                paramMap.put("TABLE_NAME", businessNumber);
//                int userTotalCount = dynamicUserDao.SelectCountAll(paramMap) + dynamicDormantUserDao.SelectCountAll(paramMap);
//                if(userTotalCount >= 5000) {
//                    return false;
//                }
//            }
//        }
//
//        return true;
//    }


    /**
     * 이용기간 기간 3일 연장
     * @param companyId
     */
//    public void UpdateValidEndThreeDays(int idx) {
//        dao.UpdateValidEndThreeDays(idx);
//    }
//
//    public List<HashMap<String, Object>> SelectCompanySendMessageList(HashMap<String, Object> paramMap) {
//        return dao.SelectCompanySendMessageList(paramMap);
//    }
//
//    public int SelectCompanySendMessageListCount(HashMap<String, Object> paramMap) {
//        return dao.SelectCompanySendMessageListCount(paramMap);
//    }






}
