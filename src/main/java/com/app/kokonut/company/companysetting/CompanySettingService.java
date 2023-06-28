package com.app.kokonut.company.companysetting;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.admin.dtos.AdminOtpKeyDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.company.companysetting.dtos.CompanySettingInfoDto;
import com.app.kokonut.company.companysettingaccessip.CompanySettingAccessIP;
import com.app.kokonut.company.companysettingaccessip.CompanySettingAccessIPRepository;
import com.app.kokonut.company.companysettingaccessip.dtos.AccessIpDeleteDto;
import com.app.kokonut.company.companysettingaccessip.dtos.CompanySettingAccessIPListDto;
import com.app.kokonut.configs.GoogleOTP;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dto.ActivityCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Woody
 * Date : 2023-06-06
 * Time :
 * Remark :
 */
@Slf4j
@Service
public class CompanySettingService {

    private final GoogleOTP googleOTP;
    private final HistoryService historyService;
    private final AdminRepository adminRepository;

    private final CompanySettingRepository companySettingRepository;
    private final CompanySettingAccessIPRepository companySettingAccessIPRepository;

    @Autowired
    public CompanySettingService(GoogleOTP googleOTP, HistoryService historyService, AdminRepository adminRepository,
                                 CompanySettingRepository companySettingRepository, CompanySettingAccessIPRepository companySettingAccessIPRepository){
        this.googleOTP = googleOTP;
        this.historyService = historyService;
        this.adminRepository = adminRepository;
        this.companySettingRepository = companySettingRepository;
        this.companySettingAccessIPRepository = companySettingAccessIPRepository;
    }

    // 서비스 설정값 정보 가져오기
    public ResponseEntity<Map<String, Object>> settingInfo(JwtFilterDto jwtFilterDto) {
        log.info("settingInfo 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        CompanySettingInfoDto companySettingInfoDto = companySettingRepository.findByCompanySettingInfo(cpCode);

//        if(companySettingInfoDto.getCsAccessSetting().equals("1")) {
            List<CompanySettingAccessIPListDto> companySettingAccessIPListDtos = companySettingAccessIPRepository.findByCompanySettingIPList(companySettingInfoDto.getCsId());
//        }

        data.put("settingInfo", companySettingInfoDto);
        data.put("accessIpList", companySettingAccessIPListDtos);

        return ResponseEntity.ok(res.success(data));
    }

    // 해외로그인 차단 서비스 설정
    @Transactional
    public ResponseEntity<Map<String, Object>> overseasBlockSetting(JwtFilterDto jwtFilterDto) throws IOException {
        log.info("overseasBlockSetting 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        Long adminId = adminCompanyInfoDto.getAdminId();
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        // 해외차단 설정 변경 코드
        ActivityCode activityCode = ActivityCode.AC_51;

        Optional<CompanySetting> optionalCompanySetting = companySettingRepository.findCompanySettingByCpCode(cpCode);
        if(optionalCompanySetting.isPresent()) {

            // 활동이력 저장 -> 비정상 모드
            String state;
            Long activityHistoryId;

            String overseasBlock = optionalCompanySetting.get().getCsOverseasBlockSetting();

            if(overseasBlock.equals("0")) {
                optionalCompanySetting.get().setCsOverseasBlockSetting("1");

                state = " 차단";
                activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                        cpCode+" - "+activityCode.getDesc()+state+" 시도 이력", "", CommonUtil.clientIp(), CommonUtil.publicIp(), 0, jwtFilterDto.getEmail());

            } else {
                optionalCompanySetting.get().setCsOverseasBlockSetting("0");

                state = " 허용";
                activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                        cpCode+" - "+activityCode.getDesc()+state+" 시도 이력", "", CommonUtil.clientIp(), CommonUtil.publicIp(), 0, jwtFilterDto.getEmail());
            }

            companySettingRepository.save(optionalCompanySetting.get());

            historyService.updateHistory(activityHistoryId,
                    cpCode+" - "+activityCode.getDesc()+state+" 시도 이력", "", 1);
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 접속허용 IP설정
    @Transactional
    public ResponseEntity<Map<String, Object>> accessSetting(JwtFilterDto jwtFilterDto) throws IOException {
        log.info("accessSetting 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        Long adminId = adminCompanyInfoDto.getAdminId();
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        // 접속허용 IP설정 변경 코드
        ActivityCode activityCode = ActivityCode.AC_52;

        Optional<CompanySetting> optionalCompanySetting = companySettingRepository.findCompanySettingByCpCode(cpCode);
        if(optionalCompanySetting.isPresent()) {
            String accessIpSetting = optionalCompanySetting.get().getCsAccessSetting();

            // 활동이력 저장 -> 비정상 모드
            String state;
            Long activityHistoryId;

            if(accessIpSetting.equals("0")) {
                optionalCompanySetting.get().setCsAccessSetting("1");

                state = " 활성화";
                activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                        cpCode+" - "+activityCode.getDesc()+state+" 시도 이력", "", CommonUtil.clientIp(), CommonUtil.publicIp(), 0, jwtFilterDto.getEmail());

            } else {
                optionalCompanySetting.get().setCsAccessSetting("0");

                state = " 비활성화";
                activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                        cpCode+" - "+activityCode.getDesc()+state+" 시도 이력", "", CommonUtil.clientIp(), CommonUtil.publicIp(), 0, jwtFilterDto.getEmail());
            }

            companySettingRepository.save(optionalCompanySetting.get());
            historyService.updateHistory(activityHistoryId,
                    cpCode+" - "+activityCode.getDesc()+state+" 시도 이력", "", 1);
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 비밀번호 변경주기 설정
    @Transactional
    public ResponseEntity<Map<String, Object>> passwordChangeSetting(JwtFilterDto jwtFilterDto, String csPasswordChangeSetting) throws IOException {
        log.info("passwordChangeSetting 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        Long adminId = adminCompanyInfoDto.getAdminId();
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        // 비밀번호 변경주기 설정 변경 코드
        ActivityCode activityCode = ActivityCode.AC_53;

        Optional<CompanySetting> optionalCompanySetting = companySettingRepository.findCompanySettingByCpCode(cpCode);
        if(optionalCompanySetting.isPresent()) {
            Long activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", CommonUtil.clientIp(), CommonUtil.publicIp(), 0, jwtFilterDto.getEmail());

            optionalCompanySetting.get().setCsPasswordChangeSetting(csPasswordChangeSetting);
            companySettingRepository.save(optionalCompanySetting.get());

            historyService.updateHistory(activityHistoryId,
                    cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 비밀번호 오류 접속제한 설정
    @Transactional
    public ResponseEntity<Map<String, Object>> passwordErrorCountSetting(JwtFilterDto jwtFilterDto, String csPasswordErrorCountSetting) throws IOException {
        log.info("passwordErrorCountSetting 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        Long adminId = adminCompanyInfoDto.getAdminId();
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        // 비밀번호 오류 접속제한 설정 변경 코드
        ActivityCode activityCode = ActivityCode.AC_54;

        Optional<CompanySetting> optionalCompanySetting = companySettingRepository.findCompanySettingByCpCode(cpCode);
        if(optionalCompanySetting.isPresent()) {
            Long activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", CommonUtil.clientIp(), CommonUtil.publicIp(), 0, jwtFilterDto.getEmail());

            optionalCompanySetting.get().setCsPasswordErrorCountSetting(csPasswordErrorCountSetting);
            companySettingRepository.save(optionalCompanySetting.get());

            historyService.updateHistory(activityHistoryId,
                    cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 자동 로그아웃 시간 설정
    @Transactional
    public ResponseEntity<Map<String, Object>> autoLogoutSetting(JwtFilterDto jwtFilterDto, String csAutoLogoutSetting) throws IOException {
        log.info("autoLogoutSetting 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        Long adminId = adminCompanyInfoDto.getAdminId();
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        // 자동 로그아웃 시간 설정 변경 코드
        ActivityCode activityCode = ActivityCode.AC_55;

        Optional<CompanySetting> optionalCompanySetting = companySettingRepository.findCompanySettingByCpCode(cpCode);
        if(optionalCompanySetting.isPresent()) {
            Long activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", CommonUtil.clientIp(), CommonUtil.publicIp(), 0, jwtFilterDto.getEmail());

            optionalCompanySetting.get().setCsAutoLogoutSetting(csAutoLogoutSetting);
            companySettingRepository.save(optionalCompanySetting.get());

            historyService.updateHistory(activityHistoryId,
                    cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 장기 미접속 접근제한 설정
    @Transactional
    public ResponseEntity<Map<String, Object>> longDisconnectionSetting(JwtFilterDto jwtFilterDto, String csLongDisconnectionSetting) throws IOException {
        log.info("longDisconnectionSetting 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        Long adminId = adminCompanyInfoDto.getAdminId();
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        // 장기 미접속 접근제한 설정 변경 코드
        ActivityCode activityCode = ActivityCode.AC_56;

        Optional<CompanySetting> optionalCompanySetting = companySettingRepository.findCompanySettingByCpCode(cpCode);
        if(optionalCompanySetting.isPresent()) {

            Long activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", CommonUtil.clientIp(), CommonUtil.publicIp(), 0, jwtFilterDto.getEmail());

            optionalCompanySetting.get().setCsLongDisconnectionSetting(csLongDisconnectionSetting);
            companySettingRepository.save(optionalCompanySetting.get());

            historyService.updateHistory(activityHistoryId,
                    cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 접속허용 IP 등록
    @Transactional
    public ResponseEntity<Map<String, Object>> accessIpSave(String csipIp, String csipRemarks, JwtFilterDto jwtFilterDto) throws IOException {
        log.info("accessIpSave 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        Long adminId = adminCompanyInfoDto.getAdminId();
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        // 접속허용 IP 등록 코드
        ActivityCode activityCode = ActivityCode.AC_57;

        Optional<CompanySetting> optionalCompanySetting = companySettingRepository.findCompanySettingByCpCode(cpCode);
        if(optionalCompanySetting.isPresent()) {

            Long activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", CommonUtil.clientIp(), CommonUtil.publicIp(), 0, jwtFilterDto.getEmail());

            if(!companySettingAccessIPRepository.existsCompanySettingAccessIPByCsIdAndCsipIp(optionalCompanySetting.get().getCsId(), csipIp)) {
                CompanySettingAccessIP companySettingAccessIP = new CompanySettingAccessIP();
                companySettingAccessIP.setCsId(optionalCompanySetting.get().getCsId());
                companySettingAccessIP.setCsipIp(csipIp);
                companySettingAccessIP.setCsipRemarks(csipRemarks);
                companySettingAccessIP.setInsert_email(jwtFilterDto.getEmail());
                companySettingAccessIP.setInsert_date(LocalDateTime.now());
                companySettingAccessIPRepository.save(companySettingAccessIP);
            } else {
                log.error("이미 등록된 허용IP 입니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO101.getCode(),ResponseErrorCode.KO101.getDesc()));
            }

            historyService.updateHistory(activityHistoryId,
                    cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 접속허용 IP 삭제
    @Transactional
    public ResponseEntity<Map<String, Object>> apiKeyIpDelete(AccessIpDeleteDto accessIpDeleteDto, JwtFilterDto jwtFilterDto) throws IOException {
        log.info("apiKeyIpDelete 호출");

        String otpValue = accessIpDeleteDto.getOtpValue();
        List<String> deleteIpList = accessIpDeleteDto.getDeleteIpList();

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
                log.error("입력된 구글 OTP 값이 일치하지 않습니다. 다시 확인해주세요.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO012.getCode(), ResponseErrorCode.KO012.getDesc()));
            }
        } else {
            log.error("등록된 OTP가 존재하지 않습니다. 구글 OTP 2단계 인증을 등록해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO011.getCode(), ResponseErrorCode.KO011.getDesc()));
        }

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        Long adminId = adminCompanyInfoDto.getAdminId();
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        // 접속허용 IP 삭제 코드
        ActivityCode activityCode = ActivityCode.AC_58;

        Optional<CompanySetting> optionalCompanySetting = companySettingRepository.findCompanySettingByCpCode(cpCode);
        if(optionalCompanySetting.isPresent()) {

            Long activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", CommonUtil.clientIp(), CommonUtil.publicIp(), 0, jwtFilterDto.getEmail());

            companySettingAccessIPRepository.findByCompanySettingAccessIPDelete(deleteIpList);

            historyService.updateHistory(activityHistoryId,
                    cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);
        }

        return ResponseEntity.ok(res.success(data));
    }
}
