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

    @ApiOperation(value = "알림톡전송 코코넛API 호출")
    @PostMapping("/alimTalkSend")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> alimTalkSend(@RequestBody HashMap<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return thirdPartyService.alimTalkSend(paramMap, jwtFilterDto);
    }





}
