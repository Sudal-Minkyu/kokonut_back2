package com.app.kokonut.bizMessage.friendtalkMessage;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.bizMessage.friendtalkMessage.dto.*;
import com.app.kokonut.bizMessage.friendtalkMessage.friendtalkMessageRecipient.FriendtalkMessageRecipient;
import com.app.kokonut.bizMessage.friendtalkMessage.friendtalkMessageRecipient.FriendtalkMessageRecipientRepository;
import com.app.kokonut.navercloud.NaverCloudPlatformService;
import com.app.kokonut.navercloud.dto.NaverCloudPlatformResultDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class FriendtalkMessageService {

    private final NaverCloudPlatformService naverCloudPlatformService;

    private final AdminRepository adminRepository;
    private final FriendtalkMessageRepository friendtalkMessageRepository;
    private final FriendtalkMessageRecipientRepository friendtalkMessageRecipientRepository;

    @Autowired
    public FriendtalkMessageService(NaverCloudPlatformService naverCloudPlatformService, AdminRepository adminRepository,
                                    FriendtalkMessageRepository friendtalkMessageRepository, FriendtalkMessageRecipientRepository friendtalkMessageRecipientRepository) {
        this.naverCloudPlatformService = naverCloudPlatformService;
        this.adminRepository = adminRepository;
        this.friendtalkMessageRepository = friendtalkMessageRepository;
        this.friendtalkMessageRecipientRepository = friendtalkMessageRecipientRepository;
    }

    // 친구톡 메시지 리스트 조회
    @Transactional
    @SuppressWarnings("unchecked")
    public ResponseEntity<Map<String, Object>> friendTalkMessageList(String email, FriendtalkMessageSearchDto friendtalkMessageSearchDto, Pageable pageable) {
        log.info("friendTalkMessageList 조회");

        AjaxResponse res = new AjaxResponse();

        // 해당 이메일을 통해 회사 fmId 조회
        Long companyId = adminRepository.findByCompanyInfo(email).getCompanyId();
        List<FriendtalkMessageInfoListDto> friendtalkMessageInfoListDtos = friendtalkMessageRepository.findByFriendtalkMessageInfoList(companyId, "1");

        List<FriendtalkMessage> alimtalkMessageList = new ArrayList<>();
        for(FriendtalkMessageInfoListDto friendtalkMessageInfoListDto : friendtalkMessageInfoListDtos) {

            NaverCloudPlatformResultDto result;

            Long fmId = friendtalkMessageInfoListDto.getFmId();
            String requestId = friendtalkMessageInfoListDto.getFmRequestId();
            String transmitType = friendtalkMessageInfoListDto.getFmTransmitType();
            String status = friendtalkMessageInfoListDto.getFmStatus();
            String updateSatus;

            log.info("status 현재 상태 : "+status);

            if(transmitType.equals("reservation")) {
                log.info("예약발송 조회");
                result = naverCloudPlatformService.getReserveState(requestId, NaverCloudPlatformService.typeFriendTalk);
                System.out.println("reservation result : " + result);
            }
            else {
                log.info("즉시발송 조회");
                result = naverCloudPlatformService.getMessages(requestId, NaverCloudPlatformService.typeFriendTalk);
                System.out.println("immediate result : " + result);
            }

            if(result.getResultCode().equals(200)) {
                JsonObject obj = (JsonObject) JsonParser.parseString(result.getResultText());

                Gson gson = new Gson();
                HashMap<String, Object> map = new HashMap<>();
                map = (HashMap<String, Object>)gson.fromJson(obj, map.getClass());

                if(transmitType.equals("reservation")) {
                    updateSatus = map.containsKey("reserveStatus")?map.get("reserveStatus").toString() : "";
                } else {
                    updateSatus = map.containsKey("statusName") ? map.get("statusName").toString() : "";
                }

                if(!updateSatus.equals("")) {
                    Optional<FriendtalkMessage> optionalFriendtalkMessage = friendtalkMessageRepository.findById(fmId);
                    if(optionalFriendtalkMessage.isPresent()){
                        optionalFriendtalkMessage.get().setFmStatus(updateSatus);
                        alimtalkMessageList.add(optionalFriendtalkMessage.get());
                    } else {
                        log.error("해당 친구톡 메세지를 찾을 수 없습니다.");
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.KO029.getCode(), ResponseErrorCode.KO029.getDesc()));
                    }
                }
            }
        }
        // 전체 업데이트
        friendtalkMessageRepository.saveAll(alimtalkMessageList);

        Page<FriendtalkMessageListDto> friendtalkMessageListDtos = friendtalkMessageRepository.findByFriendtalkMessagePage(friendtalkMessageSearchDto, companyId, pageable);

        return ResponseEntity.ok(res.ResponseEntityPage(friendtalkMessageListDtos));
    }

    // 친구톡 메시지 발송
    @Transactional
    public ResponseEntity<Map<String, Object>> postFriendMessages(String email, FriendtalkMessageSendDto friendtalkMessageSendDto, HttpServletRequest request) {
        log.info("friendTalkMessageList 조회");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        if(email.equals("test@kokonut.me")){
            log.error("FriendtalkMessageService - 체험하기모드는 이용할 수 없습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO000.getCode(), ResponseErrorCode.KO000.getDesc()));
        }
        else {
            NaverCloudPlatformResultDto result;

            MultipartFile mFile = friendtalkMessageSendDto.getMultipartFile();
            String imageId = "";
            String imageUrl = "";
            if(mFile != null) {
                log.info("친구톡 이미지 업로드 시작");

                File file = Utils.convertMultipartFileToFile(mFile);
                result = naverCloudPlatformService.setImageFile(friendtalkMessageSendDto.getKcChannelId(), file, request.getContentType());
                if(result.getResultCode().equals(200)) {
                    HashMap<String,Object> response = Utils.convertJSONstringToMap(result.getResultText());
                    imageId = response.get("imageId").toString();
                    imageUrl = response.get("imageUrl").toString();
                } else {
                    log.error("친구톡 이미지 업로드를 실패했습니다.");
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO030.getCode(), ResponseErrorCode.KO030.getDesc()));
                }
            }

            result = naverCloudPlatformService.postFriendMessages(friendtalkMessageSendDto, imageId, imageUrl);
            if(result.getResultCode().equals(200)) {
                log.info("발송성공후 친구톡 메세지 등록 정보 INSERT");

                Long companyId = adminRepository.findByCompanyInfo(email).getCompanyId();
                Long adminId = adminRepository.findByCompanyInfo(email).getAdminId();

                HashMap<String, Object> response = Utils.convertJSONstringToMap(result.getResultText());

                FriendtalkMessage friendtalkMessage = new FriendtalkMessage();
                friendtalkMessage.setCompanyId(companyId);
                friendtalkMessage.setFmRequestId(response.get("requestId").toString());
                friendtalkMessage.setKcChannelId(friendtalkMessageSendDto.getKcChannelId());
                friendtalkMessage.setFmTransmitType(friendtalkMessageSendDto.getTransmitType());
                if(friendtalkMessage.getFmTransmitType().equals("reservation")) {
                    String reservationDateStr = friendtalkMessageSendDto.getReservationDate();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                    LocalDateTime reservationDate = LocalDateTime.parse(reservationDateStr, formatter);
                    log.info("예약발송시간 : "+reservationDate);

                    friendtalkMessage.setFmReservationDate(reservationDate);
                }
                friendtalkMessage.setAdminId(adminId);
                friendtalkMessage.setInsert_email(email);
                friendtalkMessage.setInsert_date(LocalDateTime.now());

                FriendtalkMessage saveFriendtalkMessage = friendtalkMessageRepository.save(friendtalkMessage);
                log.info("친구톡 메세지 인서트 성공");

                List<FriendtalkMessageRecipient> friendtalkMessageRecipients = new ArrayList<>();

                for(FriendtalkMessageSendSubDto friendtalkMessageSendSubDto : friendtalkMessageSendDto.getFriendtalkMessageSendSubDtoList()){
                    FriendtalkMessageRecipient friendtalkMessageRecipient = new FriendtalkMessageRecipient();
                    friendtalkMessageRecipient.setFmId(saveFriendtalkMessage.getFmId());
                    friendtalkMessageRecipient.setFmrEmail(friendtalkMessageSendSubDto.getEmail());
                    friendtalkMessageRecipient.setFmrName(friendtalkMessageSendSubDto.getUserName());
                    friendtalkMessageRecipient.setFmrPhoneNumber(friendtalkMessageSendSubDto.getPhoneNumber());
                    friendtalkMessageRecipient.setInsert_email(email);
                    friendtalkMessageRecipient.setInsert_date(LocalDateTime.now());
                    friendtalkMessageRecipients.add(friendtalkMessageRecipient);
                }

                friendtalkMessageRecipientRepository.saveAll(friendtalkMessageRecipients);
                log.info("친구톡 메세지 발송인 인서트 성공");

                log.info("친구톡 발송을 성공했습니다.");
            } else {
                log.error("친구톡 발송을 실패했습니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO026.getCode(), ResponseErrorCode.KO026.getDesc()));
            }

            return ResponseEntity.ok(res.success(data));
        }
    }


}
