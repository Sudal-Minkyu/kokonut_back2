package com.app.kokonut.privacyhistory;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.Utils;
import com.app.kokonut.privacyhistory.dtos.PrivacyHistoryCode;
import com.app.kokonut.privacyhistory.dtos.PrivacyHistoryListDto;
import com.app.kokonut.privacyhistory.dtos.PrivacyHistorySearchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-05-15
 * Remark :
 */
@Slf4j
@Service
public class PrivacyHistoryService {

    private final AdminRepository adminRepository;
    private final PrivacyHistoryRepository privacyHistoryRepository;

    @Autowired
    public PrivacyHistoryService(AdminRepository adminRepository, PrivacyHistoryRepository privacyHistoryRepository) {
        this.adminRepository = adminRepository;
        this.privacyHistoryRepository = privacyHistoryRepository;
    }

    public void privacyHistoryInsert(Long adminId, PrivacyHistoryCode privacyHistoryCode, String kphIpAddr, String email) {
        log.info("privacyHistoryInsert 개인정보 처리이력 저장  호출");
        PrivacyHistory privacyHistory = new PrivacyHistory();
        privacyHistory.setAdminId(adminId);
        privacyHistory.setPrivacyHistoryCode(privacyHistoryCode);
        privacyHistory.setKphIpAddr(kphIpAddr);
        privacyHistory.setInsert_email(email);
        privacyHistory.setInsert_date(LocalDateTime.now());

        privacyHistoryRepository.save(privacyHistory);

    }

    public ResponseEntity<Map<String, Object>> privacyHistoryList(String searchText, String stime, String filterRole, String filterState,
                                                                  JwtFilterDto jwtFilterDto, Pageable pageable) {
        log.info("privacyHistoryList 호출");

        log.info("searchText : "+searchText);
        log.info("stime : "+stime);
        log.info("filterRole : "+filterRole);
        log.info("filterState : "+filterState);

        AjaxResponse res = new AjaxResponse();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());

        PrivacyHistorySearchDto privacyHistorySearchDto = new PrivacyHistorySearchDto();
        privacyHistorySearchDto.setCompanyId(adminCompanyInfoDto.getCompanyId());
        privacyHistorySearchDto.setSearchText(searchText);
        privacyHistorySearchDto.setFilterRole(filterRole);
        privacyHistorySearchDto.setFilterState(filterState);

        if(!stime.equals("")) {
            List<LocalDateTime> stimeList = Utils.getStimeList(stime);
            privacyHistorySearchDto.setStimeStart(stimeList.get(0));
            privacyHistorySearchDto.setStimeEnd(stimeList.get(1).plusHours(23).plusMinutes(59));
        }
        else {
            log.info("활동날짜 범위를 지정해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO077.getCode(), ResponseErrorCode.KO077.getDesc()));
        }

        Page<PrivacyHistoryListDto> historyListDtos = privacyHistoryRepository.findByPrivacyHistoryList(privacyHistorySearchDto, pageable);

        if(historyListDtos.getTotalPages() == 0) {
            log.info("조회된 데이터가 없습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO003.getCode(), ResponseErrorCode.KO003.getDesc()));
        }

        return ResponseEntity.ok(res.ResponseEntityPage(historyListDtos));
    }



}
