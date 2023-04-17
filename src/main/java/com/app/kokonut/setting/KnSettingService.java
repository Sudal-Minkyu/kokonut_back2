package com.app.kokonut.setting;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.Admin;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.setting.dtos.KnSettingDetailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Joy
 * Date : 2023-01-05
 * Time :
 * Remark : SettingService 관리자 환경설정 서비스
 */
@Slf4j
@Service
public class KnSettingService {

    private final KnKnSettingRepository knSettingRepository;
    private final AdminRepository adminRepository;

    public KnSettingService(KnKnSettingRepository knSettingRepository, AdminRepository adminRepository) {
        this.knSettingRepository = knSettingRepository;
        this.adminRepository = adminRepository;
    }

    public ResponseEntity<Map<String, Object>> settingDetail(String userRole, String email) {
        log.info("settingDetail 호출, userRole : " + userRole);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 접속정보에서 companyId 가져오기
        Admin admin = adminRepository.findByKnEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : " + email));
        Long companyId = admin.getCompanyId();

        log.info("관리자 환경 설정 상세 조회. companyId : " +companyId);
        KnSettingDetailDto knSettingDetailDto = knSettingRepository.findSettingDetailBycompanyId(companyId);

        if(knSettingDetailDto.getStOverseasBlock() == null){
            log.info("해외차단 아이피 기본값 세팅 0, 차단안함");
            // 해외차단 아이피 기본값 세팅 0 : (차단안함);
            knSettingDetailDto.setStOverseasBlock(0);
        }
        if(knSettingDetailDto.getStDormantAccount() == null){
            log.info("휴면회원 전환 시 정보처리방식 기본값 세팅 1, 이관 없이 삭제");
            // 휴면회원 전환 시 정보처리 기본값 세팅 1 : (이관 없이 삭제)
            knSettingDetailDto.setStDormantAccount(1);
        }
        if("[SYSTEM]".equals(userRole)){
            // 세션타임 조회
            // data.put("", "")
        }

        data.put("settingDetailDto", knSettingDetailDto);
        return ResponseEntity.ok(res.success(data));
    }

    public ResponseEntity<Map<String, Object>> settingSave(String userRole, String email, KnSettingDetailDto knSettingDetailDto) {
        log.info("settingSave 호출, userRole : " + userRole);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 접속정보에서 companyId 가져오기
        Admin admin = adminRepository.findByKnEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : " + email));
        Long companyId = admin.getCompanyId();

        if(knSettingDetailDto.getStId() == null){
            // 신규 등록
            KnSetting knSetting = new KnSetting();
            knSetting.setInsert_date(LocalDateTime.now());
            knSetting.setCompanyId(companyId);
            knSetting.setStDormantAccount(knSettingDetailDto.getStDormantAccount()); // 휴먼회원 전환 시 DB 이관 여부
            knSetting.setStOverseasBlock(knSettingDetailDto.getStOverseasBlock()); // 관리자 해외 로그인 차단 설정
            knSettingRepository.save(knSetting);
        }else{
            // 수정
            Optional<KnSetting> setting = knSettingRepository.findById(knSettingDetailDto.getStId());
            if(setting.isPresent()) {
                setting.get().setModify_date(LocalDateTime.now());
                setting.get().setStDormantAccount(knSettingDetailDto.getStDormantAccount()); // 휴먼회원 전환 시 DB 이관 여부
                setting.get().setStOverseasBlock(knSettingDetailDto.getStOverseasBlock()); // 관리자 해외 로그인 차단 설정
                knSettingRepository.save(setting.get());
            }
        }
        return ResponseEntity.ok(res.success(data));
    }

//    public ResponseEntity<Map<String, Object>> settingDelete(String userRole, String email) {
//        log.info("settingSave 호출, userRole : " + userRole);
//        // 접속정보에서 companyId 가져오기
//        Admin admin = adminRepository.findByKnEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : " + email));
//        Long companyId = admin.getCompanyId();
//
//        Integer settingdIdx = settingRepository.findSettingDetailBycompanyId(companyId).getIdx();
//        settingRepository.deleteById(settingdIdx);
//
//        return ResponseEntity.ok(res.success(data));
//    }
}
