package com.app.kokonut.thirdparty;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Map<String,Object>> bizmSetting(@RequestParam(value="settingType", defaultValue = "") String settingType,
                                                          @RequestParam(value="choseCode", defaultValue = "") String choseCode) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return thirdPartyService.bizmSetting(settingType, choseCode, jwtFilterDto);
    }





}
