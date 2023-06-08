package com.app.kokonut.company.companysetting;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.company.companysetting.dtos.CompanySettingInfoDto;
import com.app.kokonut.company.companysettingaccessip.CompanySettingAccessIPRepository;
import com.app.kokonut.company.companysettingaccessip.dtos.CompanySettingAccessIPListDto;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dto.ActivityCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    private final HistoryService historyService;
    private final AdminRepository adminRepository;

    private final CompanySettingRepository companySettingRepository;
    private final CompanySettingAccessIPRepository companySettingAccessIPRepository;

    @Autowired
    public CompanySettingService(HistoryService historyService, AdminRepository adminRepository,
                                 CompanySettingRepository companySettingRepository, CompanySettingAccessIPRepository companySettingAccessIPRepository){
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
    public ResponseEntity<Map<String, Object>> passwordChangeSetting(JwtFilterDto jwtFilterDto, String csPasswordChangeSetting) {
        log.info("accessSetting 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        Optional<CompanySetting> optionalCompanySetting = companySettingRepository.findCompanySettingByCpCode(cpCode);
        if(optionalCompanySetting.isPresent()) {
            optionalCompanySetting.get().setCsPasswordChangeSetting(csPasswordChangeSetting);
            companySettingRepository.save(optionalCompanySetting.get());
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 비밀번호 오류 접속제한 설정
    public ResponseEntity<Map<String, Object>> passwordErrorCountSetting(JwtFilterDto jwtFilterDto, String csPasswordErrorCountSetting) {
        log.info("accessSetting 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        Optional<CompanySetting> optionalCompanySetting = companySettingRepository.findCompanySettingByCpCode(cpCode);
        if(optionalCompanySetting.isPresent()) {
            optionalCompanySetting.get().setCsPasswordErrorCountSetting(csPasswordErrorCountSetting);
            companySettingRepository.save(optionalCompanySetting.get());
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 자동 로그아웃 시간 설정
    public ResponseEntity<Map<String, Object>> autoLogoutSetting(JwtFilterDto jwtFilterDto, String csAutoLogoutSetting) {
        log.info("accessSetting 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        Optional<CompanySetting> optionalCompanySetting = companySettingRepository.findCompanySettingByCpCode(cpCode);
        if(optionalCompanySetting.isPresent()) {
            optionalCompanySetting.get().setCsAutoLogoutSetting(csAutoLogoutSetting);
            companySettingRepository.save(optionalCompanySetting.get());
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 장기 미접속 접근제한 설정
    public ResponseEntity<Map<String, Object>> longDisconnectionSetting(JwtFilterDto jwtFilterDto, String csLongDisconnectionSetting) {
        log.info("longDisconnectionSetting 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        Optional<CompanySetting> optionalCompanySetting = companySettingRepository.findCompanySettingByCpCode(cpCode);
        if(optionalCompanySetting.isPresent()) {
            optionalCompanySetting.get().setCsLongDisconnectionSetting(csLongDisconnectionSetting);
            companySettingRepository.save(optionalCompanySetting.get());
        }

        return ResponseEntity.ok(res.success(data));
    }




}
