package com.app.kokonut.bizMessage.alimtalkMessage;

import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.bizMessage.alimtalkMessage.dto.AlimtalkMessageSearchDto;
import com.app.kokonut.bizMessage.alimtalkMessage.dto.AlimtalkMessageSendDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.springframework.data.web.PageableDefault;

/**
 * @author Woody
 * Date : 2022-12-19
 * Time :
 * Remark : AlimtalkMessage RestController
 */
@Slf4j
@RestController
@RequestMapping("/v2/api/AlimtalkMessage")
public class AlimtalkMessageRestController {

    private final AlimtalkMessageService alimtalkMessageService;

    @Autowired
    public AlimtalkMessageRestController(AlimtalkMessageService alimtalkMessageService) {
        this.alimtalkMessageService = alimtalkMessageService;
    }

    // 알림톡 메세지 리스트 조회
    @PostMapping(value = "/alimTalkMessageList")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> alimTalkMessageList(@RequestBody AlimtalkMessageSearchDto alimtalkTemplateSearchDto, @PageableDefault Pageable pageable) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return alimtalkMessageService.alimTalkMessageList(jwtFilterDto.getEmail(), alimtalkTemplateSearchDto, pageable);
    }

    // 알림톡 메세지 발송요청의 템플릿 리스트 조회 -> 선택한 채널ID의 템플릿 코드리스트를 반환한다.
    @GetMapping(value = "/alimTalkMessageTemplateList")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> alimTalkMessageTemplateList(@RequestParam(name="channelId") String channelId,
                                                                          @RequestParam(name="templateCode", defaultValue = "") String templateCode) throws Exception {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return alimtalkMessageService.alimTalkMessageTemplateList(jwtFilterDto.getEmail(), channelId, templateCode);
    }

    // 알림톡 메시지 발송 요청
    @GetMapping(value = "/postMessages")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> postMessages(@RequestBody AlimtalkMessageSendDto alimtalkMessageSendDto) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return alimtalkMessageService.postMessages(jwtFilterDto.getEmail(), alimtalkMessageSendDto);
    }

    // 알림톡 메시지 결과 상세정보
    @GetMapping(value = "/alimTalkMessageResultDetail") // -> 기존의 코코넛 호출 메서드명 : alimTalkTemplateStatusDescPopup
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> alimTalkMessageResultDetail(@RequestParam(name="requestId") String requestId) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return alimtalkMessageService.alimTalkMessageResultDetail(jwtFilterDto.getEmail(), requestId);
    }

    // 알림톡 메시지 보낼 유저 리스트조회
    @GetMapping(value = "/alimTalkMessageRecipientList") // -> 기존의 코코넛 호출 메서드명 : /recipient/list
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> alimTalkMessageRecipientList(@RequestParam(name="searchText", defaultValue = "") String searchText, @PageableDefault Pageable pageable) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return alimtalkMessageService.alimTalkMessageRecipientList(jwtFilterDto.getEmail(), searchText, pageable);
    }

    // 알림톡 메시지 예약발송 취소
    @PostMapping(value = "/alimTalkMessageReserveCancel") // -> 기존의 코코넛 호출 메서드명 : /reserve/cancel
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> alimTalkMessageReserveCancel(@RequestParam(name="requestId") String requestId,
                                                                           @RequestParam(name="requestId", defaultValue = "alimtalk") String type) {
        return alimtalkMessageService.alimTalkMessageReserveCancel(requestId, type);
    }

    // 알림톡 메시지 반려됬을 경우 상태 조회
    @RequestMapping(value = "/alimTalkTemplateStatusConfimInfo", method = RequestMethod.POST) // -> 기존의 코코넛 호출 메서드명 : /alimTalkTemplateStatusConfimPopup
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> alimTalkTemplateStatusConfimInfo(@RequestParam(name="channelId") String channelId,
                                                          @RequestParam(name="templateCode") String templateCode) throws Exception {
        return alimtalkMessageService.alimTalkTemplateStatusConfimInfo(channelId, templateCode);
    }

}
