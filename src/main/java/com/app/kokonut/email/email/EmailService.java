package com.app.kokonut.email.email;

import com.app.kokonut.admin.Admin;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminEmailInfoDto;
import com.app.kokonut.email.email.dtos.EmailDetailDto;
import com.app.kokonut.email.email.dtos.EmailListDto;

import com.app.kokonut.email.emailgroup.EmailGroupRepository;

import com.app.kokonut.email.emailgroup.dtos.EmailGroupAdminInfoDto;
import com.app.kokonut.email.emailgroup.dtos.EmailGroupListDto;
import com.app.kokonut.email.emailgroup.EmailGroup;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;

import com.app.kokonut.common.component.ReqUtils;

import com.app.kokonut.keydata.KeyDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Woody
 * Date : 2023-04-07
 * Remark :
 */
@Slf4j
@Service
public class EmailService {

    @Value("${kokonut.otp.hostUrl}")
    private String hostUrl; // otp_url

    private final EmailGroupRepository emailGroupRepository;
    private final AdminRepository adminRepository;
    private final EmailRepository emailRepository;
    private final MailSender mailSender;

    @Autowired
    public EmailService(KeyDataService keyDataService, EmailRepository emailRepository,
                        AdminRepository adminRepository,
                        EmailGroupRepository emailGroupRepository, MailSender mailSender) {
//        this.hostUrl = keyDataService.findByKeyValue("otp_url");
        this.emailRepository = emailRepository;
        this.adminRepository = adminRepository;
        this.emailGroupRepository = emailGroupRepository;
        this.mailSender = mailSender;
    }

    /**
     * 이메일 목록 조회
     * @param pageable 페이징 처리를 위한 정보
     */
    public ResponseEntity<Map<String,Object>> emailList(String email, String searchText, String stime, String emailType, Pageable pageable){
        log.info("emailList 호출");

        log.info("email : "+email);
        log.info("searchText : "+searchText);
        log.info("stime : "+stime);
        log.info("emailType : "+emailType);

        AjaxResponse res = new AjaxResponse();
        Page<EmailListDto> emailListDtos = emailRepository.findByEmailPage(pageable);

        return ResponseEntity.ok(res.ResponseEntityPage(emailListDtos));
    }

    /**
     * 이메일 보내기
     * @param email 페이징 처리를 위한 정보
     * @param emailDetailDto 이메일 내용
     */
    @Transactional
    public ResponseEntity<Map<String,Object>> sendEmail(String email, EmailDetailDto emailDetailDto){
        log.info("### sendEmail 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 접속한 사용자 인덱스
        Admin admin = adminRepository.findByKnEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : "+email));
        emailDetailDto.setEmSenderAdminId(admin.getAdminId());

        // 이메일 전송을 위한 전처리 - filter, unfilter
        String title = ReqUtils.filter(emailDetailDto.getEmTitle());
        String originContents = ReqUtils.filter(emailDetailDto.getEmContents()); // ReqUtils.filter 처리 <p> -- > &lt;p&gt;, html 태그를 DB에 저장하기 위해 이스케이프문자로 치환
        String contents = ReqUtils.unFilter(emailDetailDto.getEmContents()); // &lt;br&gt;이메일내용 --> <br>이메일내용, html 화면에 뿌리기 위해 특수문자를 치환
        log.info("### unFilter After content : " + contents);

        // 이메일 전송을 위한 전처리 - 첨부 이미지 경로 처리
        String imgSrcToken = "src=\"";
        int index = contents.indexOf(imgSrcToken);
        if(index > -1){
            StringBuilder sb = new StringBuilder();
            sb.append(contents);
            sb.insert(index + imgSrcToken.length(), hostUrl);
            contents = sb.toString();
        }

        // 이메일 전송을 위한 준비 - reciverType에 따른 adminIdList 구하기
        String receiverType = emailDetailDto.getEmReceiverType();
        String adminIdList = "";

        if("I".equals(receiverType)){
            adminIdList = emailDetailDto.getEmReceiverType().toString();
        }else if(("G").equals(receiverType)){
            Long emailGroupIdx = emailDetailDto.getEgId();
            EmailGroupAdminInfoDto emailGroupAdminInfoDto;
            emailGroupAdminInfoDto = emailGroupRepository.findEmailGroupAdminInfoByIdx(emailGroupIdx);
            adminIdList = emailGroupAdminInfoDto.getEgAdminIdList();
        }else{
            log.error("### 받는사람 타입(I:개별,G:그룹)을 알 수 없습니다. :" + receiverType);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO040.getCode(), ResponseErrorCode.KO040.getDesc()));
        }

        // mailSender 실질적인 이메일 전송 부분
        String[] toks = adminIdList.split(",");
        for(String tok : toks){
            AdminEmailInfoDto adminEmailInfoDto = adminRepository.findByKnEmailInfo(Long.valueOf(tok));
            if(adminEmailInfoDto != null){
                String reciverEmail = adminEmailInfoDto.getKnEmail();
                String reciverName = adminEmailInfoDto.getKnName();

                log.info("### mailSender을 통해 건별 이메일 전송 시작");
                log.info("### reciver idx : "+tok + ", senderEmail : " +email+", reciverEmail : "+ reciverEmail);
                boolean mailSenderResult = mailSender.sendMail(reciverEmail, reciverName, title, contents);
                if(mailSenderResult){
                    // mailSender 성공
                    log.error("### 메일전송 성공했습니다.. reciver admin idx : "+ tok);
                }else{
                    // mailSender 실패
                    log.error("### 해당 메일 전송에 실패했습니다. 관리자에게 문의하세요. reciver admin idx : "+ tok+", reciverEmail : "+ reciverEmail);
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO041.getCode(), ResponseErrorCode.KO041.getDesc()));
                }
            }else{
                // TODO 일부가 탈퇴하고 일부는 이메일 정보가 있을때 처리에 대한 고민
                log.error("### 해당 idx에 해당하는 회원 이메일을 찾을 수 없습니다. reciver admin idx : "+ tok);
            }
        }

        // 전송 이력 저장 처리 - originContents로 DB 저장
        log.info("### 이메일 이력 저장 처리");
        Email reciveEmail = new Email();

        emailDetailDto.setEmContents(originContents);
        reciveEmail.setEmSenderAdminId(emailDetailDto.getEmSenderAdminId());
        reciveEmail.setEmReceiverType(emailDetailDto.getEmReceiverType());
        reciveEmail.setEmTitle(emailDetailDto.getEmTitle());
        reciveEmail.setEmContents(emailDetailDto.getEmContents());

        // 조건에 따른 분기 처리
        if("I".equals(emailDetailDto.getEmReceiverType()) && emailDetailDto.getEmReceiverAdminIdList() != null) {
            reciveEmail.setEmReceiverAdminIdList(emailDetailDto.getEmReceiverAdminIdList());
        }

        // 조건에 따른 분기 처리
        if("G".equals(emailDetailDto.getEmReceiverType()) && emailDetailDto.getEgId() != null) {
            reciveEmail.setEgId(emailDetailDto.getEgId());
        }
        reciveEmail.setInsert_date(LocalDateTime.now());

        // save or update
        Email sendEmail = emailRepository.save(reciveEmail);

        log.info("### 이메일 이력 저장 처리 완료");

        // TODO 정상적으로 저장된 경우를 확인하는 방법 알아보기. save 처리가 되던 update 처리가 되던 결과적으로 해당 인덱스는 존재함.
        // sendEamil 객체에서 reciverType에 따라 어드민 인덱스를 조회, 해당 인덱스로 어드민 이메일을 확인한 다음 해당 이메일로 받는 내역을 조회한 다음. 해당 건수가 존재하면 받은걸로 친다고하기엔.
        // 하지만 이런 방법으로 할 경우 이전
        if(emailRepository.existsByEmId(sendEmail.getEmId())){
            log.info("### 이메일 이력 저장에 성공했습니다. : "+sendEmail.getEmId());
            return ResponseEntity.ok(res.success(data));
        }else{
            log.error("### 이메일 이력 저장에 실패했습니다. : "+sendEmail.getEmId());
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO041.getCode(), ResponseErrorCode.KO041.getDesc()));
        }

    }

    /**
     * 이메일 상세보기
     * @param idx email의 idx
     */
    public ResponseEntity<Map<String,Object>> sendEmailDetail(Long idx){
        log.info("### sendEmailDetail 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        if(idx == null){
            log.error("### 이메일 호출할 idx가 존재 하지 않습니다. : "+idx);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO031.getCode(), ResponseErrorCode.KO031.getDesc()));
        } else {
            log.info("### 이메일 상세보기 idx : "+idx);
            // 이메일 인덱스로 이메일 정보 조회
            EmailDetailDto emailDetailDto = emailRepository.findEmailByIdx(idx);
            if(emailDetailDto != null){
                String receiverType = emailDetailDto.getEmReceiverType();
                String adminIdList = "";
                if("I".equals(receiverType)){
                    // 개별 선택으로 발송한 경우
                    adminIdList = emailDetailDto.getEmReceiverAdminIdList().toString();
                }else if("G".equals(receiverType)){
                    // 그룹 선택으로 메일을 발송한 경우
                    Long emailGroupIdx = emailDetailDto.getEgId();
                    // 메일 그룹 조회 쿼리 동작
                    EmailGroupAdminInfoDto emailGroupAdminInfoDto = emailGroupRepository.findEmailGroupAdminInfoByIdx(emailGroupIdx);
                    adminIdList = emailGroupAdminInfoDto.getEgAdminIdList();
                }else{
                    log.error("### 받는사람 타입(I:개별,G:그룹)을 알 수 없습니다. :" + receiverType);
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO040.getCode(), ResponseErrorCode.KO040.getDesc()));
                }

                // 받는 사람 이메일 문자열 조회
                StringBuilder emailList = new StringBuilder();
                String[] toks = adminIdList.split(",");
                for(int i = 0; i < toks.length; i++) {
                    String tok = toks[i];
                    AdminEmailInfoDto adminEmailInfoDto = adminRepository.findByKnEmailInfo(Long.valueOf(tok));
                    if(adminEmailInfoDto != null){
                        emailList.append(adminEmailInfoDto.getKnEmail());
                        if(i < toks.length - 1) {
                            emailList.append(", ");
                        }
                    }else{ // kokonut_1@kokonut.me, kokonut_2@kokonut.me, 탈퇴한 사용자, kokonut_4@kokonut.me 형태로 이메일 문자열 반환, 추후 변경될 수 있음.
                        emailList.append("탈퇴한 사용자");
                        if(i < toks.length - 1) {
                            emailList.append(", ");
                        }
                    }
                }
                data.put("emailList", emailList); // 받는 사람 이메일 문자열
                data.put("title", emailDetailDto.getEmTitle()); // 제목
                data.put("contents", emailDetailDto.getEmContents()); // 내용
            } else {
                log.error("### 이메일 정보가 존재 하지 않습니다. : "+idx);
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO031.getCode(), ResponseErrorCode.KO031.getDesc()));
            }
            return ResponseEntity.ok(res.success(data));
        }
    }

    /**
     * 이메일 발송 대상 목록 조회하기
     * @param pageable 페이징 처리를 위한 정보
     */
    public ResponseEntity<Map<String, Object>> emailTargetGroupList(Pageable pageable) {
        log.info("### emailTargetGroupList 호출");
        /*
         * 그룹명 : 미식플랫폼 00 관리자
         * 설 명 : 미식플랫폼 00의 관리자 그룹
         * 관리자 Idx : 2, 4, 5 ...
         * 이메일 : a001@00.oo.com, a002@00.oo.com, a003@00.oo.com
         *
         * findEmailGroupDatils()를 조회한다
         * 해당 결과에서 가져온 관리자 인덱스를 가지고 관리자 이메일을 조회한다.
         * EmailGroup Entity 클래스를 가지는 List에 각 값을 넣어서 던져준다.
         */

        AjaxResponse res = new AjaxResponse();

        EmailGroup emailGroup = new EmailGroup(); // TEMP
        List<EmailGroupListDto> resultDto = emailGroupRepository.findEmailGroupDetails();

        List<EmailGroup> resultList = new ArrayList<>();
        for(int i = 0; i<resultDto.size(); i++){
            emailGroup.setEgId(resultDto.get(i).getEgId());
            emailGroup.setEgName(resultDto.get(i).getEgName());
            emailGroup.setEgDesc(resultDto.get(i).getEgDesc());
            emailGroup.setEgAdminIdList(resultDto.get(i).getEgAdminIdList());

            String adminIds = resultDto.get(i).getEgAdminIdList();
            String adminIdList[] = adminIds.split(",");

            List<String> emailList = new ArrayList<>();
            for (String adminId : adminIdList) {
                String adminEmail = adminRepository.findByKnEmailInfo(Long.parseLong(adminId)).getKnEmail();
                emailList.add(adminEmail); // a001@00.oo.com, a002@00.oo.com, a003@00.oo.com
            }
            StringBuilder adminEmailList = new StringBuilder();
            for(int j = 0; j < emailList.size(); j++){
                adminEmailList.append(emailList.get(j));
                if(j == emailList.size()-1){
                    // 마지막
                    adminEmailList.append("");
                }else {
                    adminEmailList.append(",");
                }
            }
            String stAdminEmailList = adminEmailList.toString();
            emailGroup.setAdminEmailList(stAdminEmailList);
            resultList.add(emailGroup);
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
            log.info(">>>> idx: "+resultList.get(i).getEgId());
            log.info(">>>> name: "+resultList.get(i).getEgName());
            log.info(">>>> Desc: "+resultList.get(i).getEgDesc());
            log.info(">>>> adminId: "+resultList.get(i).getEgAdminIdList());
            log.info(">>>> adminEmail: "+resultList.get(i).getAdminEmailList());
            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ");
        }
        

        Page resultPage = new PageImpl<>(resultList, pageable, resultList.size());
        log.info("결과 List size : "+resultList.size());

    return ResponseEntity.ok(res.ResponseEntityPage(resultPage));
    }
}
