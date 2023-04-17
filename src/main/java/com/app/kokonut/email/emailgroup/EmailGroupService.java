package com.app.kokonut.email.emailgroup;

import com.app.kokonut.email.emailgroup.dtos.EmailGroupAdminInfoDto;
import com.app.kokonut.email.emailgroup.dtos.EmailGroupDetailDto;
import com.app.kokonut.email.emailgroup.dtos.EmailGroupListDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class EmailGroupService {
    private final EmailGroupRepository emailGroupRepository;

    public EmailGroupService(EmailGroupRepository emailGroupRepository) {
        this.emailGroupRepository = emailGroupRepository;
    }

    /***
     * 메일 그룹 adminIdList 조회
     * @param egId - email_group egId
     * 기존 코코넛 서비스 SelectEmailGroupByegId
     */
    public ResponseEntity<Map<String,Object>> emailGroupDetail(Long egId){
        log.info("### findEmailGroupadminIdByegId 호출");
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        if(egId == null){
            log.error("### 해당 메일 그룹을 찾을 수 없습니다. : "+egId);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO042.getCode(), ResponseErrorCode.KO042.getDesc()));
        }else{
            EmailGroupAdminInfoDto emailGroupAdminInfoDto = emailGroupRepository.findEmailGroupAdminInfoByIdx(egId);
            data.put("adminIdList", emailGroupAdminInfoDto.getEgAdminIdList());

            return ResponseEntity.ok(res.success(data));
        }
    }

    /***
     * 메일 그룹 목록 조회
     * 기존 코코넛 서비스 SelectEmailGroupList
     */
    public ResponseEntity<Map<String,Object>> emailGroupList(Pageable pageable){
        log.info("### emailGroupList 호출");

        AjaxResponse res = new AjaxResponse();
        Page<EmailGroupListDto> emailGroupListDto = emailGroupRepository.findEmailGroupPage(pageable);

        return ResponseEntity.ok(res.ResponseEntityPage(emailGroupListDto));
    }


    /***
     * 메일 그룹 등록
     * 기존 코코넛 서비스 InsertEmailGroup
     */
    public ResponseEntity<Map<String,Object>> saveEmailGroup(EmailGroupDetailDto emailGroupDetailDto) {
        log.info("### saveEmailGroup 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("### 이메일 그룹 저장");
        EmailGroup newEmailGroup =  new EmailGroup();
        newEmailGroup.setEgName(emailGroupDetailDto.getEgName());
        newEmailGroup.setEgDesc(emailGroupDetailDto.getEgDesc());
        newEmailGroup.setEgAdminIdList(emailGroupDetailDto.getEgAdminIdList());
        emailGroupRepository.save(newEmailGroup);

        return ResponseEntity.ok(res.success(data));
    }

    /***
     * 메일 그룹 삭제
     * @param egId - email_group egId
     * 기존 코코넛 서비스 DeleteEmailGroupUseYn
     */
    public ResponseEntity<Map<String,Object>> deleteEmailGroup(Long egId) {
        log.info("### deleteEmailGroup 호출");
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        if(egId == null){
            log.error("### 삭제할 이메일 그룹의 egId가 존재하지 않습니다. : "+egId);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO042.getCode(), ResponseErrorCode.KO042.getDesc()));
        } else {
            log.info("### 이메일 그룹 삭제");
            EmailGroup deleteEmailGroup =  new EmailGroup();
            deleteEmailGroup.setEgUseYn("N");
            deleteEmailGroup.setEgId(egId);
            emailGroupRepository.save(deleteEmailGroup);
            log.info("### 이메일 그룹 삭제, 삭제된 이메일 그룹 egId : " + egId);
            return ResponseEntity.ok(res.success(data));
        }
    }

    /***
     * 메일 그룹 수정
     * 기존 코코넛 서비스 UpdateEmailGroup
     */
    public ResponseEntity<Map<String,Object>> UpdateEmailGroup(EmailGroupDetailDto emailGroupDetailDto) {
        log.info("### UpdateEmailGroup 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("### 이메일 그룹 수정");
        EmailGroup updateEmailGroup =  new EmailGroup();
        updateEmailGroup.setEgId(emailGroupDetailDto.getEgId());
        updateEmailGroup.setEgName(emailGroupDetailDto.getEgName());
        updateEmailGroup.setEgDesc(emailGroupDetailDto.getEgDesc());
        updateEmailGroup.setEgAdminIdList(emailGroupDetailDto.getEgAdminIdList());
        emailGroupRepository.save(updateEmailGroup);
        log.info("### 이메일 그룹 수정, 수정된 이메일 그룹 egId : " + emailGroupDetailDto.getEgId());
        return ResponseEntity.ok(res.success(data));

    }

}
