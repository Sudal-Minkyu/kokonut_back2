package com.app.kokonut.bizMessage.kakaoChannel;

import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.bizMessage.kakaoChannel.dto.KakaoChannelSearchDto;
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
 * Date : 2022-12-16
 * Time :
 * Remark : KakaoChannel RestController
 */
@Slf4j
@RestController
@RequestMapping("/v2/api/KakaoChannel")
public class KakaoChannelRestController {

    private final KakaoChannelService kakaoChannelService;

    @Autowired
    public KakaoChannelRestController(KakaoChannelService kakaoChannelService) {
        this.kakaoChannelService = kakaoChannelService;
    }

    // 카카오 채널 조회 -> 수정작업이 들어가서 Post로 설정
    @PostMapping(value = "/kakaoTalkChannelList")
        @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> kakaoTalkChannelList(@RequestBody KakaoChannelSearchDto kakaoChannelSearchDto, @PageableDefault Pageable pageable) throws Exception {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return kakaoChannelService.kakaoTalkChannelList(jwtFilterDto.getEmail(), kakaoChannelSearchDto, pageable);
    }

    // 카카오톡 채널확인
    @GetMapping(value = "/postKakaoTalkChannels")
        @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> postKakaoTalkChannels(@RequestParam(name="channelId", defaultValue = "") String channelId,
                                                                    @RequestParam(name="adminTelNo", defaultValue = "") String adminTelNo) throws Exception {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return kakaoChannelService.postKakaoTalkChannels(jwtFilterDto.getEmail(), channelId, adminTelNo);
    }

    // 본인이증 확인확인 + 카카오톡 채널등록
    @PostMapping(value = "/kakaoTalkchannelToken")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> kakaoTalkchannelToken(@RequestParam(name="channelId", defaultValue = "") String channelId,
                                                                    @RequestParam(name="token", defaultValue = "") String token) throws Exception {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return kakaoChannelService.kakaoTalkchannelToken(jwtFilterDto.getEmail(), channelId, token);
    }

    // 카카오톡 채널 삭제
    @PostMapping(value = "/deleteKakaoTalkChannels")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> deleteKakaoTalkChannels(@RequestParam(name="channelId", defaultValue = "") String channelId) throws Exception {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return kakaoChannelService.deleteKakaoTalkChannels(jwtFilterDto.getEmail(), channelId);
    }


}
