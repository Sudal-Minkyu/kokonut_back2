package com.app.kokonutapi.thirdparty;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.thirdparty.ThirdPartyService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-07-24
 * Time :
 * Remark : 서드파티 KokonutAPI 컨트롤러
 */
@Slf4j
@RequestMapping("/v3/api/ThirdParty")
@RestController
public class ThirdPartyApiRestController {

    private final ThirdPartyService thirdPartyService;

    @Autowired
    public ThirdPartyApiRestController(ThirdPartyService thirdPartyService){
        this.thirdPartyService = thirdPartyService;
    }

    @ApiOperation(value="알림톡전송 코코넛API 호출", notes="" +
            "<br>1. 알림톡을 전송하기에 앞서 최소정보를 받는다.<br>" +
            "2. 프로파일키(profileKey)), 템플릿코드(templateCode), 보낼타입(sendType)<br>" +
            "3. 성공적으로 발송했다면 성공한건수와 실패건수를 반환한다.<br>" +
            "<br>" +
            "* 모의해킹 테스트용 호출 데이터(아래) *<br><br>" +
            "x-api-key <br>" +
            "2a40c544978b48b374dfc91a2d2dfc72<br><br>" +
            "paramMap : <br>" +
            "{\n" +
            "\"profileKey\" : \"f1d0081b74b59cedc2648ea2da6fa6788e26c181\",\n" +
            "\"templateCode\" : \"template_basic_01\",\n" +
            "\"sendType\" : \"ALL\",\n" +
            "\"codeList\" : {\n" +
            "\"고객\" : \"코코넛\"\n" +
            "}\n" +
            "}")
    @PostMapping("/alimTalkSend")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> alimTalkSend(@RequestBody HashMap<String, Object> paramMap, HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return thirdPartyService.alimTalkSend(paramMap, jwtFilterDto);
    }





}
