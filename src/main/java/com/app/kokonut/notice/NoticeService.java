package com.app.kokonut.notice;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.Admin;
import com.app.kokonut.notice.dtos.*;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Joy
 * Date : 2022-12-27
 * Time :
 * Remark : NoticeService 공지사항 서비스
 */
@Slf4j
@Service
public class NoticeService {
    private final AdminRepository adminRepository;
    private final NoticeRepository noticeRepository;
    public NoticeService(NoticeRepository noticeRepository,
                         AdminRepository adminRepository) {
        this.noticeRepository = noticeRepository;
        this.adminRepository = adminRepository;
    }

    public ResponseEntity<Map<String, Object>> noticeList(String userRole, NoticeSearchDto noticeSearchDto, Pageable pageable) {
        log.info("noticeList 호출, userRole : "+ userRole);
        AjaxResponse res = new AjaxResponse();
        if("[SYSTEM]".equals(userRole)){
            log.info("공지사항 목록 조회");
            Page<NoticeListDto> noticeListDtos = noticeRepository.findNoticePage(noticeSearchDto, pageable);
            return ResponseEntity.ok(res.ResponseEntityPage(noticeListDtos));
        }else{
            log.info("공지사항 목록(제목+내용, 게시중) 조회");
            Page<NoticeContentListDto> noticeListDtos = noticeRepository.findNoticeContentPage(pageable);
            return ResponseEntity.ok(res.ResponseEntityPage(noticeListDtos));
        }
    }
    public ResponseEntity<Map<String, Object>> noticeDetail(String userRole, Long ntId) {
        log.info("noticeDetail 호출, userRole" + userRole);
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        if("[SYSTEM]".equals(userRole)){
            if(ntId != null){
                log.info("공지사항 상세 조회, ntId : " + ntId);
                NoticeDetailDto noticeDetailDto = noticeRepository.findNoticeByIdx(ntId);
                if(noticeDetailDto != null){
                    log.info("공지사항 상세 조회 성공, ntId : " + noticeDetailDto.getNtId() + ", Title : " + noticeDetailDto.getNtTitle());
                    data.put("noticeDetailDto",  noticeDetailDto);

                    // viewCount 증가
                    Notice notice = noticeRepository.findById(ntId)
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 'Admin' 입니다."));
                    notice.setNtViewCount(notice.getNtViewCount()+1);
                    noticeRepository.save(notice);

                    return ResponseEntity.ok(res.success(data));
                }else{
                    log.error("공지사항 상세 조회 실패, ntId : "+ntId);
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO048.getCode(), ResponseErrorCode.KO048.getDesc()));
                }
            }else{
                log.error("ntId 값을 확인 할 수 없습니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO047.getCode(), ResponseErrorCode.KO047.getDesc()));
            }
        }else{
            log.error("접근 권한이 없습니다. userRole : " + userRole);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO001.getCode(), ResponseErrorCode.KO001.getDesc()));
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> noticeSave(String userRole, String email, NoticeDetailDto noticeDetailDto) {
        log.info("noticeSave 호출, userRole : "+ userRole);
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        if("[SYSTEM]".equals(userRole)){
            // 접속 정보에서 관리자 정보 가져오기, ntId, name
            Admin admin = adminRepository.findByKnEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : "+email));
            Long adminId = admin.getAdminId();
            String adminName = admin.getKnName();

            if(noticeDetailDto.getNtId() == null){
                log.info("공지사항 등록");
                NoticeDetailDto insertDetailDto = noticeDetailDto;

                log.info("등록자, 등록일시, 내용 세팅");
                Notice insertNotice = new Notice();
                insertNotice.setAdminId(adminId);
                insertNotice.setNtRegisterName(adminName);
                insertNotice.setInsert_email(email);
                insertNotice.setInsert_date(LocalDateTime.now());

                insertNotice.setNtIsNotice(insertDetailDto.getNtIsNotice());
                insertNotice.setNtRegistDate(insertDetailDto.getRegistDate());
                insertNotice.setNtTitle(insertDetailDto.getNtTitle());
                insertNotice.setNtContent(insertDetailDto.getNtContent());
                insertNotice.setNtState(1);
                Long savedntId = noticeRepository.save(insertNotice).getNtId();
                log.info("공지사항 등록 완료. ntId : " + savedntId);
            }else{
                log.info("공지사항 수정, ntId : " + noticeDetailDto.getNtId());
                Optional<Notice> updateNotice = noticeRepository.findById(noticeDetailDto.getNtId());
                if(updateNotice.isEmpty()){
                    log.error("공지사항 수정 실패, 게시글을 발견할 수 없습니다. 요청 ntId : " + noticeDetailDto.getNtId());
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO048.getCode(), ResponseErrorCode.KO048.getDesc()));
                }else{
                    log.info("수정자, 수정일시 세팅 내용 세팅");
                    updateNotice.get().setModify_id(adminId);
                    updateNotice.get().setModify_email(email);
                    updateNotice.get().setModify_date(LocalDateTime.now());

                    log.info("수정 내용 세팅");
                    if(noticeDetailDto.getNtIsNotice() != null){
                        updateNotice.get().setNtIsNotice(noticeDetailDto.getNtIsNotice());
                    }
                    if(noticeDetailDto.getRegistDate() != null){
                        updateNotice.get().setNtRegistDate(noticeDetailDto.getRegistDate());
                    }
                    if(noticeDetailDto.getNtTitle() != null){
                        updateNotice.get().setNtTitle(noticeDetailDto.getNtTitle());
                    }
                    if(noticeDetailDto.getNtContent() != null){
                        updateNotice.get().setNtContent(noticeDetailDto.getNtContent());
                    }
                    if(noticeDetailDto.getNtState() != null){
                        updateNotice.get().setNtState(noticeDetailDto.getNtState());
                    }

                    Long updatedntId = noticeRepository.save(updateNotice.get()).getNtId();
                    log.info("공지사항 수정 완료. ntId : " + updatedntId);
                }
            }
            return ResponseEntity.ok(res.success(data));
        }else{
            log.error("접근권한이 없습니다. userRole : " + userRole);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO001.getCode(), ResponseErrorCode.KO001.getDesc()));
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> noticeDelete(String userRole, String email, Long ntId) {
        log.info("noticeDelete 호출, userRole : " +userRole);
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        if("[SYSTEM]".equals(userRole)){
            if(ntId != null){
                log.info("공지사항 삭제 시작.");
                noticeRepository.deleteById(ntId);
                log.info("공지사항 삭제 완료. ntId : "+ntId);
                return ResponseEntity.ok(res.success(data));
            }else{
                log.error("ntId 값을 확인 할 수 없습니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO047.getCode(), ResponseErrorCode.KO047.getDesc()));
            }
        }else {
            log.error("접근권한이 없습니다. userRole : " + userRole);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO001.getCode(), ResponseErrorCode.KO001.getDesc()));
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> noticeState(String userRole, String email, NoticeStateDto noticeStateDto) {
        log.info("noticeDelete 호출, userRole : " +userRole);
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        if("[SYSTEM]".equals(userRole)){
            if(noticeStateDto.getNtId() != null){
                log.info("공지사항 상태변경 시작.");
                Notice notice = noticeRepository.findById(noticeStateDto.getNtId())
                        .orElseThrow(() -> new UsernameNotFoundException("해당하는 공지사항을 찾을 수 없습니다. : "+noticeStateDto.getNtId()));

                Integer state = noticeStateDto.getNtState();
                if(state == 1){
                    notice.setNtStopDate(LocalDateTime.MIN);
                }else{
                    notice.setNtStopDate(LocalDateTime.now());
                }

                notice.setNtState(state);
                noticeRepository.save(notice);
                log.info("공지사항 상태변경 완료.");
                return ResponseEntity.ok(res.success(data));
            }else{
                log.error("ntId 값을 확인 할 수 없습니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO047.getCode(), ResponseErrorCode.KO047.getDesc()));
            }
        }else {
            log.error("접근권한이 없습니다. userRole : " + userRole);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO001.getCode(), ResponseErrorCode.KO001.getDesc()));
        }
    }
}
