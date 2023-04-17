package com.app.kokonut.bizMessage.kakaoChannel;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.bizMessage.alimtalkTemplate.AlimtalkTemplateRepository;
import com.app.kokonut.bizMessage.kakaoChannel.dto.KakaoChannelListDto;
import com.app.kokonut.bizMessage.kakaoChannel.dto.KakaoChannelSearchDto;
import com.app.kokonut.navercloud.NaverCloudPlatformService;
import com.app.kokonut.navercloud.dto.NaverCloudPlatformResultDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Woody
 * Date : 2022-12-16
 * Time :
 * Remark : KakaoChannel Service
 */
@Slf4j
@Service
public class KakaoChannelService {

    private final NaverCloudPlatformService naverCloudPlatformService;

    private final AdminRepository adminRepository;
    private final KakaoChannelRepository kakaoChannelRepository;
    private final AlimtalkTemplateRepository alimtalkTemplateRepository;

    @Autowired
    public KakaoChannelService(NaverCloudPlatformService naverCloudPlatformService, AdminRepository adminRepository, KakaoChannelRepository kakaoChannelRepository, AlimtalkTemplateRepository alimtalkTemplateRepository) {
        this.naverCloudPlatformService = naverCloudPlatformService;
        this.adminRepository = adminRepository;
        this.kakaoChannelRepository = kakaoChannelRepository;
        this.alimtalkTemplateRepository = alimtalkTemplateRepository;
    }

    @Transactional
    public ResponseEntity<Map<String,Object>> kakaoTalkChannelList(String email, KakaoChannelSearchDto kakaoChannelSearchDto, Pageable pageable) throws Exception {

        log.info("kakaoTalkChannelList 호출");

        AjaxResponse res = new AjaxResponse();

        // 해당 이메일을 통해 회사 IDX 조회
        Long companyId = adminRepository.findByCompanyInfo(email).getCompanyId();

        // 사용자가 체험하기를 제외한 모든 채널 조회후 업데이트??..
        if(!email.equals("test@kokonut.me")) {
            List<HashMap<String, Object>> channelList = naverCloudPlatformService.getChannels();
            for(HashMap<String, Object> channel : channelList) {
                String channelId = channel.get("channelId").toString();
                String channelStatus = channel.get("channelStatus").toString();
                log.info("channelId : "+channelId);
                log.info("channelStatus : "+channelStatus);

                // 테스트 후 작성하기 - 22/12/15 to. Woody
//                if("DELETING_PERMANENTLY".equals(channelStatus) )
//                {
//                    String channelName = "DELETING_PERMANENTLY";
//                    alimTalkMessageService.UpdateKakaoTalkChannel(channelId, channelName, channelStatus);
//
//                } else
//                {
//                    String channelName = channel.get("channelName").toString();
//                    alimTalkMessageService.UpdateKakaoTalkChannel(channelId, channelName, channelStatus);
//                }
            }
        }

        Page<KakaoChannelListDto> kakaoChannelListDtos = kakaoChannelRepository.findByKakaoChannelPage(kakaoChannelSearchDto, companyId, pageable);

        return ResponseEntity.ok(res.ResponseEntityPage(kakaoChannelListDtos));
    }


    public ResponseEntity<Map<String, Object>> postKakaoTalkChannels(String email, String channelId, String adminTelNo) throws Exception {

        log.info("postKakaoTalkChannels 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        if(email.equals("test@kokonut.me")){
            System.out.println("체험하기모드는 할 수 없습니다.");
        }

        if(channelId.isEmpty()) {
            log.error("채널ID를 입력해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO017.getCode(), ResponseErrorCode.KO017.getDesc()));
        }

        if(adminTelNo.isEmpty()) {
            log.error("전화번호를 입력해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO018.getCode(), ResponseErrorCode.KO018.getDesc()));
        }

        NaverCloudPlatformResultDto naverCloudPlatformResultDto = naverCloudPlatformService.postChannels(channelId, adminTelNo);
        log.info("naverCloudPlatformResultDto : "+naverCloudPlatformResultDto);

        data.put("resultCode", naverCloudPlatformResultDto.getResultCode());

        return ResponseEntity.ok(res.success(data));
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> kakaoTalkchannelToken(String email, String channelId, String token) throws Exception {

        log.info("postKakaoTalkChannels 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        if(email.equals("test@kokonut.me")){
            System.out.println("체험하기모드는 할 수 없습니다.");
        }

        if(channelId.isEmpty()) {
            log.error("채널ID를 입력해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO017.getCode(), ResponseErrorCode.KO017.getDesc()));
        }

        if(token.isEmpty()) {
            log.error("인증번호를 입력해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO019.getCode(), ResponseErrorCode.KO019.getDesc()));
        }

        NaverCloudPlatformResultDto naverCloudPlatformResultDto = naverCloudPlatformService.postChannelToken(channelId, token);
        log.info("naverCloudPlatformResultDto : "+naverCloudPlatformResultDto);

        if(naverCloudPlatformResultDto.getResultCode().equals(200)) {
            log.info("관리자 승인을 위해 차단으로 상태값 변경");

            // 관리자 승인을 위해 차단으로 상태값 변경
            naverCloudPlatformResultDto = naverCloudPlatformService.patchChannelStatus(channelId, "INACTIVE");

            if(naverCloudPlatformResultDto.getResultCode().equals(200)) {
                // 카카오 채널 등록 INSERT
                Long companyId = adminRepository.findByCompanyInfo(email).getCompanyId();

                KakaoChannel kakaoChannel = new KakaoChannel();
                kakaoChannel.setKcChannelId(channelId);
                kakaoChannel.setCompanyId(companyId);
                kakaoChannel.setKcStatus("INACTIVE");
                kakaoChannel.setInsert_email(email);
                kakaoChannel.setInsert_date(LocalDateTime.now());
                kakaoChannelRepository.save(kakaoChannel);
            } else {
                log.error("관리자 승인을 위해 차단으로 상태값 변경 실패");
            }
        }

        data.put("resultCode", naverCloudPlatformResultDto.getResultCode());

        return ResponseEntity.ok(res.success(data));
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> deleteKakaoTalkChannels(String email, String channelId) throws Exception {

        log.info("deleteKakaoTalkChannels 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        if(email.equals("test@kokonut.me")){
            System.out.println("체험하기모드는 할 수 없습니다.");
        }

        if(channelId.isEmpty()) {
            log.error("채널ID를 입력해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO017.getCode(), ResponseErrorCode.KO017.getDesc()));
        }

        NaverCloudPlatformResultDto naverCloudPlatformResultDto = naverCloudPlatformService.deleteChannels(channelId);

        if(naverCloudPlatformResultDto.getResultCode().equals(200)) {
            log.info("카카오 채널 삭제 성공");

            // 채널 삭제
            kakaoChannelRepository.findByKakaoChannelDelete(channelId);

            // 해당 채널로 작성된 템플릿 삭제
            // -> 채널삭제전 알림문구 중요하게 알려야 할 듯? ex) "해당 채널을 삭제하시면 등록된 템플릿은 모두 삭제되면 복구할 수 없습니다. 그래도 진행하시겠습니까?"
            alimtalkTemplateRepository.findByAlimtalkTemplateDelete(channelId);

            log.info("기존의 카카오채널과 등록된 템플릿 삭제 성공");
        } else {
            log.error("카카오 채널 삭제를 실패했습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO020.getCode(), ResponseErrorCode.KO020.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }


}
