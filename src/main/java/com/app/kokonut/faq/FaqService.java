package com.app.kokonut.faq;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.Admin;
import com.app.kokonut.faq.dtos.FaqAnswerListDto;
import com.app.kokonut.faq.dtos.FaqDetailDto;
import com.app.kokonut.faq.dtos.FaqListDto;
import com.app.kokonut.faq.dtos.FaqSearchDto;
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

@Slf4j
@Service
public class FaqService {
    private final AdminRepository adminRepository;
    private final FaqRepository faqRepository;
    public FaqService(FaqRepository faqRepository,
                      AdminRepository adminRepository) {
        this.faqRepository = faqRepository;
        this.adminRepository = adminRepository;
    }
    public ResponseEntity<Map<String, Object>> faqList(String userRole, FaqSearchDto faqSearchDto, Pageable pageable) {
        log.info("faqList 호출, userRole : "+ userRole);
        AjaxResponse res = new AjaxResponse();
        if("[SYSTEM]".equals(userRole)){
            log.info("자주묻는 질문 목록 조회");
            Page<FaqListDto> faqListDtos = faqRepository.findFaqPage(faqSearchDto, pageable);
            return ResponseEntity.ok(res.ResponseEntityPage(faqListDtos));
        }else{
            log.info("자주묻는 질문 목록(질문+답변, 게시중) 조회");
            Page<FaqAnswerListDto> faqListDtos = faqRepository.findFaqAnswerPage(pageable);
            return ResponseEntity.ok(res.ResponseEntityPage(faqListDtos));
        }
    }

    public ResponseEntity<Map<String, Object>> faqDetail(String userRole, Long faqId) {
        log.info("faqDetail 호출, userRole : "+ userRole);
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        if("[SYSTEM]".equals(userRole)){
            if(faqId != null){
                log.info("자주묻는 질문 상세 조회, faqId : " + faqId);
                FaqDetailDto faqDetailDto = faqRepository.findFaqByIdx(faqId);
                if(faqDetailDto != null){
                    // 조회 성공
                    log.info("자주묻는 질문 상세 조회 성공, faqId : " + faqDetailDto.getFaqId() + ", Question : " + faqDetailDto.getFaqQuestion());
                    data.put("faqDetailDto",  faqDetailDto);
                    return ResponseEntity.ok(res.success(data));
                }else{
                    // 조회 실패
                    log.error("자주묻는 질문 상세 조회 실패, faqId : " +faqId);
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO050.getCode(), ResponseErrorCode.KO050.getDesc()));
                }
            }else{
                log.error("faqId 값을 확인 할 수 없습니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO049.getCode(), ResponseErrorCode.KO049.getDesc()));
            }
        }else{
            log.error("접근권한이 없습니다. userRole : " + userRole);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO001.getCode(), ResponseErrorCode.KO001.getDesc()));
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> faqSave(String userRole, String email, FaqDetailDto faqDetailDto) {
        log.info("faqSave 호출, userRole : "+userRole);
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        if("[SYSTEM]".equals(userRole)){
            // 접속 정보에서 관리자 정보 가져오기, faqId, name
            Admin admin = adminRepository.findByKnEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : "+email));
            Long adminId = admin.getAdminId();
            String adminName = admin.getKnName();

            if(faqDetailDto.getFaqId() != null){
                log.info("자주묻는 질문 등록");
                FaqDetailDto insertDetailDto = faqDetailDto;
                log.info("등록자, 등록일시, 내용 세팅");
                Faq insertFaq = new Faq();

                insertFaq.setAdminId(adminId);
                insertFaq.setInsert_email(email);
                insertFaq.setInsert_date(LocalDateTime.now());

                insertFaq.setFaqQuestion(insertDetailDto.getFaqQuestion());
                insertFaq.setFaqAnswer(insertDetailDto.getFaqAnswer());
                insertFaq.setFaqType(insertDetailDto.getFaqType());

                Long savedfaqId = faqRepository.save(insertFaq).getFaqId();
                log.info("자주묻는 질문 등록 완료. faqId : " + savedfaqId);
            }else{
                log.info("자주묻는 질문 수정, faqId : " + faqDetailDto.getFaqId());
                Optional<Faq> updateFaq = faqRepository.findById(faqDetailDto.getFaqId());
                if(updateFaq.isEmpty()){
                    log.error("자주묻는 질문 수정 실패, 게시글을 발견할 수 없습니다. 요청 faqId : " + faqDetailDto.getFaqId());
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO050.getCode(), ResponseErrorCode.KO050.getDesc()));
                }else{
                    log.info("수정자, 수정일시 세팅");
                    updateFaq.get().setModify_id(adminId);
                    updateFaq.get().setModify_email(email);
                    updateFaq.get().setModify_date(LocalDateTime.now());
                    log.info("내용 세팅");
                    if(faqDetailDto.getFaqQuestion() != null){
                        updateFaq.get().setFaqQuestion(faqDetailDto.getFaqQuestion());
                    }
                    if(faqDetailDto.getFaqAnswer() != null){
                        updateFaq.get().setFaqAnswer(faqDetailDto.getFaqAnswer());
                    }
                    if(faqDetailDto.getFaqType() != null){
                        updateFaq.get().setFaqType(faqDetailDto.getFaqType());
                    }
                    Long updatedfaqId = faqRepository.save(updateFaq.get()).getFaqId();
                    log.info("자주묻는 질문 수정 완료. faqId : " + updatedfaqId);
                }
            }
            return ResponseEntity.ok(res.success(data));
        }else{
            log.error("접근권한이 없습니다. userRole : " + userRole);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO001.getCode(), ResponseErrorCode.KO001.getDesc()));
        }
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> faqDelete(String userRole, String email, Long faqId) {
        log.info("faqDelete 호출, userRole : " +userRole);
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        if("[SYSTEM]".equals(userRole)){
            if(faqId != null){
                log.info("자주묻는 질문 삭제 시작.");
                faqRepository.deleteById(faqId);
                log.info("자주묻는 질문 삭제 완료. faqId : "+faqId);
                return ResponseEntity.ok(res.success(data));
            }else{
                log.error("faqId 값을 확인 할 수 없습니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO049.getCode(), ResponseErrorCode.KO049.getDesc()));
            }
        }else {
            log.error("접근권한이 없습니다. userRole : " + userRole);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO001.getCode(), ResponseErrorCode.KO001.getDesc()));
        }
    }
}
