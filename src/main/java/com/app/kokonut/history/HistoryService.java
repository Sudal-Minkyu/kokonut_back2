package com.app.kokonut.history;

import com.app.kokonut.admin.Admin;
import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.Utils;
import com.app.kokonut.configs.ExcelService;
import com.app.kokonut.history.dto.*;
import com.app.kokonut.totalDBDownloadHistory.TotalDbDownloadHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Woody
 * Date : 2022-11-03
 * Remark :
 */
@Slf4j
@Service
public class HistoryService {

    private final AdminRepository adminRepository;

    private final HistoryRepository historyRepository;

    private final ExcelService excelService;
    private final TotalDbDownloadHistoryRepository downloadHistoryRepository;

    @Autowired
    public HistoryService(AdminRepository adminRepository, HistoryRepository historyRepository,
                          ExcelService excelService, TotalDbDownloadHistoryRepository downloadHistoryRepository) {
        this.adminRepository = adminRepository;
        this.historyRepository = historyRepository;
        this.excelService = excelService;
        this.downloadHistoryRepository = downloadHistoryRepository;
    }

    // Column 리스트 조회
    public List<Column> findByHistoryColumnList() {
        return historyRepository.findByHistoryColumnList();
    }

    // 관리자 활동이력 리스트 ahType => "2"
    public ResponseEntity<Map<String,Object>> activityList(String email, String searchText, String stime, String actvityType, Pageable pageable) {
        log.info("actvityList 호출");

        log.info("email : "+email);
        log.info("searchText : "+searchText);
        log.info("stime : "+stime);
        log.info("actvityType : "+actvityType);

        AjaxResponse res = new AjaxResponse();

        // 접속한 사용자 인덱스
        Admin admin = adminRepository.findByKnEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : "+email));

        HistorySearchDto historySearchDto = new HistorySearchDto();
        historySearchDto.setCompanyId(admin.getCompanyId());
        historySearchDto.setSearchText(searchText);

        if(!actvityType.equals("")) {
            List<ActivityCode> activityCodeList = new ArrayList<>();
            String[] employeeNamesSplit = actvityType.split(",");
            ArrayList<String> actvityTypeList = new ArrayList<>(Arrays.asList(employeeNamesSplit));

            for(String actvityCode : actvityTypeList) {
                activityCodeList.add(ActivityCode.valueOf(actvityCode));
            }
            historySearchDto.setActivityCodeList(activityCodeList);
        }

        if(!stime.equals("")) {
            List<LocalDateTime> stimeList = Utils.getStimeList(stime);
            historySearchDto.setStimeStart(stimeList.get(0));
            historySearchDto.setStimeEnd(stimeList.get(1).plusHours(23).plusMinutes(59));
        }
        else {
            log.info("활동날짜 범위를 지정해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO077.getCode(), ResponseErrorCode.KO077.getDesc()));
        }

        Page<HistoryListDto> historyListDtos = historyRepository.findByHistoryList(historySearchDto, pageable);

        if(historyListDtos.getTotalPages() == 0) {
            log.info("조회된 데이터가 없습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO003.getCode(), ResponseErrorCode.KO003.getDesc()));
        }

        return ResponseEntity.ok(res.ResponseEntityPage(historyListDtos));
    }

    // 관리자 처리이력 리스트 ahType => "1"
    public ResponseEntity<Map<String,Object>> findByProcessHistoryList(String email, String searchText, String stime, String actvityType, Pageable pageable) {
        log.info("findByHistoryList 호출");

        log.info("email : "+email);
        log.info("searchText : "+searchText);
        log.info("stime : "+stime);
        log.info("actvityType : "+actvityType);

        AjaxResponse res = new AjaxResponse();

        // 접속한 사용자 인덱스
        Admin admin = adminRepository.findByKnEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : "+email));

        HistorySearchDto HistorySearchDto = new HistorySearchDto();
        HistorySearchDto.setCompanyId(admin.getCompanyId());
        HistorySearchDto.setSearchText(searchText);

        if(!actvityType.equals("")) {
            List<ActivityCode> activityCodeList = new ArrayList<>();
            String[] employeeNamesSplit = actvityType.split(",");
            ArrayList<String> actvityTypeList = new ArrayList<>(Arrays.asList(employeeNamesSplit));

            for(String actvityCode : actvityTypeList) {
                activityCodeList.add(ActivityCode.valueOf(actvityCode));
            }
            HistorySearchDto.setActivityCodeList(activityCodeList);
        }

        if(!stime.equals("")) {
            List<LocalDateTime> stimeList = Utils.getStimeList(stime);
            HistorySearchDto.setStimeStart(stimeList.get(0));
            HistorySearchDto.setStimeEnd(stimeList.get(1).plusHours(23).plusMinutes(59));
        }
        else {
            log.info("활동날짜 범위를 지정해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO077.getCode(), ResponseErrorCode.KO077.getDesc()));
        }

        Page<HistoryListDto> historyListDtos = historyRepository.findByHistoryList(HistorySearchDto, pageable);
        if(historyListDtos.getTotalPages() == 0) {
            log.info("조회된 데이터가 없습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO003.getCode(), ResponseErrorCode.KO003.getDesc()));
        }

        return ResponseEntity.ok(res.ResponseEntityPage(historyListDtos));
    }

    // 해당 유저의 최근 접속(로그인) 날짜 + 접속IP 가져오기
    public HistoryLoginInfoDto findByLoginHistory(String knEmail) {
        if(knEmail == null) {
            return null;
        }
        return historyRepository.findByLoginHistory(knEmail);
    }


    // 활동내역 정보 리스트 조회
    public List<HistoryInfoListDto> findByHistoryBycompanyIdAndTypeList(Long companyId, Integer ahType) {
        return historyRepository.findByHistoryBycompanyIdAndTypeList(companyId, ahType);
    }

//    public List<Map<String, Object>> SelectHistoryList(Integer type, Long companyId){
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("TYPE", type);
//        map.put("COMPANY_IDX", companyId);
//
//        return HistoryRepository.SelectByTypeAndcompanyId(map);
//    }

//    /**
//     * 활동내역 리스트 Count
//     */
//    public int SelectHistoryListCount(HashMap<String, Object> paramMap) {
//        return HistoryRepository.SelectHistoryListCount(paramMap);
//    }

    /**
     * 활동내역 상세보기
     * @param idx
     */
    public HistoryDto findByHistoryByIdx(Long idx) {
        return historyRepository.findByHistoryByIdx(idx);
    }

    /**
     * 활동내역 insert
     * @param ahType - 1:고객정보처리, 2:관리자활동, 3:회원DB관리이력, 4:정보제공이력
     * @param adminId
     * @param activityCode
     * @param ahActivityDetail - 활동상세내역
     * @param ahReason - 사유
     * @param ahIpAddr - 접속IP주소
     * @param ahState - 0:비정상, 1:정상
     * @return save IDX
     * 기존 코코넛 : InsertHistory
     */
    public Long insertHistory(int ahType, Long adminId, ActivityCode activityCode,
                                      String ahActivityDetail, String ahReason, String ahIpAddr, int ahState, String email) {

        History History = new History();
        History.setAhType(ahType);
        History.setAdminId(adminId);
        History.setActivityCode(activityCode);
        History.setAhActivityDetail(ahActivityDetail);
        History.setAhReason(ahReason);
        History.setAhIpAddr(ahIpAddr);
        History.setAhState(ahState);
        History.setInsert_email(email);
        History.setInsert_date(LocalDateTime.now());

        History = historyRepository.save(History);

        return History.getAhId();
    }

    /**
     * 활동내역 Update
     * @param ahId - 키값
     * @param activityDetail - 활동상세내역
     * @param ahReason - 사유
     * @param ahState - 0:비정상, 1:정상
     * 기존 코코넛 : UpdateHistory
     */
    public void updateHistory(Long ahId, String activityDetail, String ahReason, int ahState) {
        History history = historyRepository.findById(ahId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 'activityCode' 입니다."));

        history.setAhActivityDetail(activityDetail);
        history.setAhReason(ahReason);
        history.setAhState(ahState);

        historyRepository.save(history);
    }

    /**
     * 활동내역 삭제
     * @param idx
     */
//    public void DeleteHistoryByIdx(int idx) {
//        HistoryRepository.DeleteHistoryByIdx(idx);
//    }
    public void deleteHistoryByIdx(Long idx) {
        History History = historyRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 'History' 입니다."));

        historyRepository.delete(History);
    }

    /**
     * 활동내역 사유 변경
     * @param ahId
     * @param ahReason
     */
//    public void UpdateHistoryReasonByIdx(HashMap<String, Object> paramMap) {
//        HistoryRepository.UpdateHistoryReasonByIdx(paramMap);
//    }
    public void updateHistoryReasonByIdx(Long ahId, String ahReason) {
        History History = historyRepository.findById(ahId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 'History' 입니다."));
        History.setAhReason(ahReason);
        historyRepository.save(History);
    }

    /**
     * 활동내역 상세보기 (companyId, reason, activityIdx)
     * @param companyId
     * @param ahReason
     */
    public HistoryDto findByHistoryBycompanyIdAndReasonaAndAtivityIdx(Long companyId, String ahReason) {
        return historyRepository.findByHistoryBycompanyIdAndReasonaAndAtivityIdx(companyId, ahReason);
    }

    /**
     * 활동내역 통계
     * @param companyId
     * @param day - 날짜
     */
    public HistoryStatisticsDto findByHistoryStatistics(Long companyId, int day) {
        return historyRepository.findByHistoryStatistics(companyId, day);
    }

    /**
     * 유효기간 지난 활동내역 삭제
     * @param activityIdx 활동IDX
     * @param month 달
     */
//    public void DeleteExpiredHistory(int activityIdx, int month) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("activityIdx", activityIdx);
//        map.put("month", month);
//
//        HistoryRepository.DeleteExpiredHistory(map);
//    }
    public void deleteExpiredHistory(int activityIdx, int month) {
        historyRepository.deleteExpiredHistory(activityIdx, month);
    }




}
