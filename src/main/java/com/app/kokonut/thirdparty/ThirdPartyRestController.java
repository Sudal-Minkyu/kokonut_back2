package com.app.kokonut.thirdparty;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-07-24
 * Time :
 * Remark : 서드파티 컨트롤러
 */
@Slf4j
@RequestMapping("/v2/api/ThirdParty")
@RestController
public class ThirdPartyRestController {

    private final ThirdPartyService thirdPartyService;

    @Autowired
    public ThirdPartyRestController(ThirdPartyService thirdPartyService){
        this.thirdPartyService = thirdPartyService;
    }

    @ApiOperation(value = "비즈엠 서드파티 셋팅")
    @PostMapping(value = "/bizmSetting")
    @ApiImplicitParam(name ="Authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> bizmSetting(@RequestParam(value="tsBizmReceiverNumCode", defaultValue = "") String tsBizmReceiverNumCode,
                                                          @RequestParam(value="tsBizmAppUserIdCode", defaultValue = "") String tsBizmAppUserIdCode) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return thirdPartyService.bizmSetting(tsBizmReceiverNumCode, tsBizmAppUserIdCode, jwtFilterDto);
    }

    @ApiOperation(value = "비즈엠 서드파티에 지정된 항목 가져오기")
    @GetMapping(value = "/bizmGetCode")
    @ApiImplicitParam(name ="Authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> bizmGetCode() {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return thirdPartyService.bizmGetCode(jwtFilterDto);
    }



}
