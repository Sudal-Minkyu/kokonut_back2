package com.app.kokonut.bizMessage.alimtalkMessage;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.bizMessage.alimtalkMessage.alimtalkMessageRecipient.AlimtalkMessageRecipient;
import com.app.kokonut.bizMessage.alimtalkMessage.alimtalkMessageRecipient.AlimtalkMessageRecipientRepository;
import com.app.kokonut.bizMessage.alimtalkMessage.dto.*;
import com.app.kokonut.bizMessage.alimtalkTemplate.AlimtalkTemplateRepository;
import com.app.kokonut.bizMessage.alimtalkTemplate.dto.AlimtalkMessageTemplateInfoListDto;
import com.app.kokonut.navercloud.dto.NaverCloudPlatformResultDto;
import com.app.kokonut.navercloud.NaverCloudPlatformService;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Woody
 * Date : 2022-12-19
 * Time :
 * Remark : AlimtalkMessage Service
 */
@Slf4j
@Service
public class AlimtalkMessageService {

    private final NaverCloudPlatformService naverCloudPlatformService;

    private final AdminRepository adminRepository;

    private final AlimtalkMessageRepository alimtalkMessageRepository;
    private final AlimtalkMessageRecipientRepository alimtalkMessageRecipientRepository;

    private final AlimtalkTemplateRepository alimtalkTemplateRepository;

    @Autowired
    public AlimtalkMessageService(NaverCloudPlatformService naverCloudPlatformService, AdminRepository adminRepository, AlimtalkMessageRepository alimtalkMessageRepository, AlimtalkMessageRecipientRepository alimtalkMessageRecipientRepository, AlimtalkTemplateRepository alimtalkTemplateRepository) {
        this.naverCloudPlatformService = naverCloudPlatformService;
        this.adminRepository = adminRepository;
        this.alimtalkMessageRepository = alimtalkMessageRepository;
        this.alimtalkMessageRecipientRepository = alimtalkMessageRecipientRepository;
        this.alimtalkTemplateRepository = alimtalkTemplateRepository;
    }

    // 알림톡 메세지 리스트 조회
    @Transactional
    @SuppressWarnings("unchecked")
    public ResponseEntity<Map<String, Object>> alimTalkMessageList(String email, AlimtalkMessageSearchDto alimtalkMessageSearchDto, Pageable pageable) {
        log.info("alimTalkMessageList 조회");

        AjaxResponse res = new AjaxResponse();

        // 해당 이메일을 통해 회사 amId 조회
        Long companyId = adminRepository.findByCompanyInfo(email).getCompanyId();
        List<AlimtalkMessageInfoListDto> alimtalkMessageInfoListDtos = alimtalkMessageRepository.findByAlimtalkMessageInfoList(companyId, "1");

        List<AlimtalkMessage> alimtalkMessageList = new ArrayList<>();
        for(AlimtalkMessageInfoListDto alimtalkMessageInfoListDto : alimtalkMessageInfoListDtos) {

            NaverCloudPlatformResultDto result;

            Long amId = alimtalkMessageInfoListDto.getAmId();
            String amRequestId = alimtalkMessageInfoListDto.getAmRequestId();
            String amTransmitType = alimtalkMessageInfoListDto.getAmTransmitType();
            String amStatus = alimtalkMessageInfoListDto.getAmStatus();
            String updateSatus;

            log.info("amRequestId 현재 상태 : "+amRequestId);

            if(amTransmitType.equals("reservation")) {
                log.info("예약발송 조회");
                result = naverCloudPlatformService.getReserveState(amRequestId, NaverCloudPlatformService.typeAlimTalk);
                System.out.println("reservation result : " + result);
            } else {
                log.info("즉시발송 조회");
                result = naverCloudPlatformService.getMessages(amRequestId, NaverCloudPlatformService.typeAlimTalk);
                System.out.println("immediate result : " + result);
            }

            if(result.getResultCode().equals(200)) {
                JsonObject obj = (JsonObject) JsonParser.parseString(result.getResultText());

                Gson gson = new Gson();
                HashMap<String, Object> map = new HashMap<>();
                map = (HashMap<String, Object>)gson.fromJson(obj, map.getClass());

                if(amTransmitType.equals("reservation")) {
                    updateSatus = map.containsKey("reserveStatus")?map.get("reserveStatus").toString() : "";
                } else {
                    updateSatus = map.containsKey("statusName") ? map.get("statusName").toString() : "";
                }

                if(!updateSatus.equals("")) {
                    Optional<AlimtalkMessage> optionalAlimtalkMessage = alimtalkMessageRepository.findById(amId);
                    if(optionalAlimtalkMessage.isPresent()){
                        optionalAlimtalkMessage.get().setAmStatus(updateSatus);
                        alimtalkMessageList.add(optionalAlimtalkMessage.get());
                    } else {
                        log.error("해당 알림톡 메세지를 찾을 수 없습니다.");
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.KO023.getCode(), ResponseErrorCode.KO023.getDesc()));
                    }
                }
            }
        }

        // 전체 업데이트
        alimtalkMessageRepository.saveAll(alimtalkMessageList);

        Page<AlimtalkMessageListDto> alimtalkMessageListDtos = alimtalkMessageRepository.findByAlimtalkMessagePage(alimtalkMessageSearchDto, companyId, pageable);

        return ResponseEntity.ok(res.ResponseEntityPage(alimtalkMessageListDtos));
    }

    // 알림톡 메세지 발송요청의 템플릿 리스트 조회 -> 선택한 채널ID의 템플릿 코드리스트를 반환한다.
    public ResponseEntity<Map<String, Object>> alimTalkMessageTemplateList(String email, String channelId, String templateCode) throws Exception {
        log.info("alimTalkMessageTemplateList 조회");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        Long companyId = adminRepository.findByCompanyInfo(email).getCompanyId();

        log.info("channelId : "+channelId);
        log.info("templateCode : "+templateCode);

        List<HashMap<String, Object>> templates = new ArrayList<>();
        List<HashMap<String, Object>> template = new ArrayList<>();

        List<AlimtalkMessageTemplateInfoListDto> alimtalkMessageTemplateInfoListDtos = alimtalkTemplateRepository.findByAlimtalkMessageTemplateInfoList(companyId, channelId);

        for(AlimtalkMessageTemplateInfoListDto alimtalkMessageTemplateInfoListDto : alimtalkMessageTemplateInfoListDtos) {

            if(templateCode == null) {
                templateCode = alimtalkMessageTemplateInfoListDto.getAtTemplateCode();
            }

            NaverCloudPlatformResultDto result = naverCloudPlatformService.getTemplates(channelId, templateCode, "");

            if(result.getResultCode().equals(200)) {
                ObjectMapper mapper = new ObjectMapper();
                template = mapper.readValue(result.getResultText(), new TypeReference<>() {});

                String messageType = alimtalkMessageTemplateInfoListDto.getAtTemplateCode();
                template.get(0).put("messageType", messageType);

                if("EX".equals(messageType) || "MI".equals(messageType) ) {
                    String extraContent = alimtalkMessageTemplateInfoListDto.getAtExtraContent();
                    template.get(0).put("extraContent", extraContent);
                }
                if("AD".equals(messageType) || "MI".equals(messageType) ) {
                    String adContent = alimtalkMessageTemplateInfoListDto.getAtAdContent();
                    template.get(0).put("adContent", adContent);
                }

                String emphasizeType = alimtalkMessageTemplateInfoListDto.getAtEmphasizeType();
                template.get(0).put("emphasizeType", emphasizeType);

                if("TEXT".equals(emphasizeType)) {
                    String emphasizeTitle = alimtalkMessageTemplateInfoListDto.getAtEmphasizeTitle();
                    String emphasizeSubTitle = alimtalkMessageTemplateInfoListDto.getAtEmphasizeSubTitle();
                    template.get(0).put("emphasizeTitle", emphasizeTitle);
                    template.get(0).put("emphasizeSubTitle", emphasizeSubTitle);
                }
                templates.add(template.get(0));
            }
        }

        data.put("templates", templates);

        return ResponseEntity.ok(res.success(data));
    }

    // 알림톡 메시지 발송 요청
    @Transactional
    public ResponseEntity<Map<String, Object>> postMessages(String email, AlimtalkMessageSendDto alimtalkMessageSendDto) {
        log.info("postMessages 조회");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 수신자가 아무도 없을 경우
        if(alimtalkMessageSendDto.getAlimtalkMessageSendSubDtoList().size() == 0) {
            log.error("발송할 수신자를 선택해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO025.getCode(), ResponseErrorCode.KO025.getDesc()));
        }

        // 예약발송 일 경우 -> 발송시간설정은 필수값임
        if(alimtalkMessageSendDto.getAmTransmitType().equals("reservation") && alimtalkMessageSendDto.getAmReservationDate() == null) {
            log.error("예약발송일 경우 보낼시간을 설정해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO024.getCode(), ResponseErrorCode.KO024.getDesc()));
        }

        NaverCloudPlatformResultDto result = naverCloudPlatformService.postMessages(alimtalkMessageSendDto);
        if(result.getResultCode().equals(200)) {
            log.info("발송성공후 알림톡 메세지 등록 정보 INSERT");

            Long adminId = adminRepository.findByCompanyInfo(email).getAdminId();

            HashMap<String, Object> response = Utils.convertJSONstringToMap(result.getResultText());

            AlimtalkMessage alimTalkMessage = new AlimtalkMessage();
            alimTalkMessage.setAdminId(adminId);
            alimTalkMessage.setAmRequestId(response.get("requestId").toString());
            alimTalkMessage.setKcChannelId(alimtalkMessageSendDto.getKcChannelId());
            alimTalkMessage.setAmTransmitType(alimtalkMessageSendDto.getAmTransmitType());
            if(alimtalkMessageSendDto.getAmTransmitType().equals("reservation")) {
                String reservationDateStr = alimtalkMessageSendDto.getAmReservationDate();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                LocalDateTime reservationDate = LocalDateTime.parse(reservationDateStr, formatter);
                log.info("예약발송시간 : "+reservationDate);

                alimTalkMessage.setAmReservationDate(reservationDate);
            }
            alimTalkMessage.setInsert_email(email);
            alimTalkMessage.setInsert_date(LocalDateTime.now());

            AlimtalkMessage saveAlimtalkMessage = alimtalkMessageRepository.save(alimTalkMessage);
            log.info("알림톡 메세지 인서트 성공");

            List<AlimtalkMessageRecipient> alimtalkMessageRecipients = new ArrayList<>();

            for(AlimtalkMessageSendSubDto alimtalkMessageSendSubDto : alimtalkMessageSendDto.getAlimtalkMessageSendSubDtoList()){
                AlimtalkMessageRecipient alimtalkMessageRecipient = new AlimtalkMessageRecipient();
                alimtalkMessageRecipient.setAmId(saveAlimtalkMessage.getAmId());
                alimtalkMessageRecipient.setAmr_email(alimtalkMessageSendSubDto.getEmail());
                alimtalkMessageRecipient.setAmr_name(alimtalkMessageSendSubDto.getUserName());
                alimtalkMessageRecipient.setAmr_phone_number(alimtalkMessageSendSubDto.getPhoneNumber());
                alimtalkMessageRecipients.add(alimtalkMessageRecipient);
            }

            alimtalkMessageRecipientRepository.saveAll(alimtalkMessageRecipients);
            log.info("알림톡 메세지 발송인 인서트 성공");

            log.info("알림톡 발송을 성공했습니다.");
        } else {
            log.error("알림톡 발송을 실패했습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO026.getCode(), ResponseErrorCode.KO026.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 알림톡 메시지 결과 상세정보
    @SuppressWarnings("unchecked")
    public ResponseEntity<Map<String, Object>> alimTalkMessageResultDetail(String email, String requestId) {
        log.info("alimTalkMessageResultDetail 조회");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("결과 호출 requestId : "+requestId);

        AlimtalkMessageResultDetailDto alimtalkMessage = alimtalkMessageRepository.findByAlimtalkMessageResultDetail(requestId);
        if(alimtalkMessage == null){
            log.error("알림톡메세지 결과정보가 존재하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO027.getCode(), ResponseErrorCode.KO027.getDesc()));
        }
        else {
            if(!email.equals("test@kokonut.me")){

                NaverCloudPlatformResultDto result = naverCloudPlatformService.getMessages(requestId, NaverCloudPlatformService.typeAlimTalk);

                if(result.getResultCode().equals(200)) {
                    int totalCount = 0;
                    int successCount = 0;
                    int failCount = 0;
                    int processingCount = 0;
                    int reservedCount = 0;
                    int canceledCount = 0;

                    HashMap<String,Object> response = Utils.convertJSONstringToMap(result.getResultText());

                    List<HashMap<String, Object>> messages = (List<HashMap<String, Object>>) response.get("messages");

                    for(HashMap<String, Object> message : messages) {
                        String messageStatusName = message.get("messageStatusName").toString();
                        if("success".equals(messageStatusName) || "done".equals(messageStatusName)) {
                            successCount++;
                        } else if("fail".equals(messageStatusName) || "stale".equals(messageStatusName)) {
                            failCount++;
                        } else if("processing".equals(messageStatusName)) {
                            processingCount++;
                        } else if("reserved".equals(messageStatusName)) {
                            reservedCount++;
                        } else if("canceled".equals(messageStatusName)) {
                            canceledCount++;
                        }
                        totalCount++;
                    }

                    data.put("result", response);
                    data.put("totalCount", totalCount);
                    data.put("successCount", successCount);
                    data.put("failCount", failCount);
                    data.put("processingCount", processingCount);
                    data.put("reservedCount", reservedCount);
                    data.put("canceledCount", canceledCount);
                    data.put("experience", false);
                }
            }
            else{
                // 체험하기 값 보내주기
                HashMap<String, Object> message = new HashMap<>();
                message.put("to","01022223334");
                message.put("requestStatusDesc","성공");
                message.put("messageStatusDesc","정상 발송");
                message.put("content","안녕하세요. 체험하기 모드입니다.");

                data.put("result", message);

                Random random = new Random();
                data.put("experience", true);
                data.put("totalCount", random.nextInt(6)+random.nextInt(6));
                data.put("successCount", random.nextInt(6));
                data.put("failCount", random.nextInt(4));
                data.put("processingCount", random.nextInt(2));
                data.put("reservedCount", random.nextInt(2));
                data.put("canceledCount", random.nextInt(2));
            }

            data.put("alimtalkMessage", alimtalkMessage);
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 알림톡 메시지 보낼 유저 리스트조회 -> 유저정보 조회해오는거 완료되면 시작할 것 - 2022/12/20 to.woody
    public ResponseEntity<Map<String, Object>> alimTalkMessageRecipientList(String email, String searchText, Pageable pageable) {
        log.info("alimTalkMessageRecipientList 조회");

        AjaxResponse res = new AjaxResponse();

        log.info("조회내용 searchText : "+searchText);

        String companyCode = adminRepository.findByCompanyInfo(email).getCompanyCode();

        log.info("호출 할 유저리스트의 기업번호 : "+companyCode);

//        Page<Dto> listDtos = dynamicUserService.SelectRecipientList(searchText, businessNumber, pageable);

//        return ResponseEntity.ok(res.ResponseEntityPage(listDtos));
        return null;

    }

    // 알림톡 메시지 예약발송 취소
    public ResponseEntity<Map<String, Object>> alimTalkMessageReserveCancel(String requestId, String type) {
        log.info("alimTalkMessageReserveCancel 조회");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("예약취소 할 requestId : "+requestId);

        NaverCloudPlatformResultDto result = naverCloudPlatformService.reserveMessage(requestId, type);

        if(result.getResultCode().equals(200)) {
            return ResponseEntity.ok(res.success(data));
        }
        else {
            log.error("예약발송 취소를 실패했습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO028.getCode(), ResponseErrorCode.KO028.getDesc()));
        }
    }

    // 알림톡 메시지 반려됬을 경우 상태 조회
    public ResponseEntity<Map<String, Object>> alimTalkTemplateStatusConfimInfo(String channelId, String templateCode) throws Exception {
        log.info("alimTalkTemplateStatusConfimInfo 조회");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        NaverCloudPlatformResultDto result = naverCloudPlatformService.getTemplates(channelId, templateCode, "");

        if(result.getResultCode().equals(200)) {
            List<Map<String, Object>> response = new ObjectMapper().readValue(result.getResultText(), new TypeReference<>() {});
            Object comments = response.get(0).get("comments");
            data.put("comments", comments);
        }

        return ResponseEntity.ok(res.success(data));
    }

}
