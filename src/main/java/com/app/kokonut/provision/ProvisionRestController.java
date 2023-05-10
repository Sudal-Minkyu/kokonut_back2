package com.app.kokonut.provision;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.provision.dtos.ProvisionSaveDto;
import com.app.kokonutapi.personalInfoProvision.dtos.PersonalInfoProvisionSaveDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-01-17
 * Remark :
 */
@Slf4j
@RestController
@RequestMapping("/v2/api/PersonalInfoProvision")
public class ProvisionRestController {

    private final ProvisionService provisionService;

    @Autowired
    public ProvisionRestController(ProvisionService provisionService){
        this.provisionService = provisionService;
    }

    @PostMapping("/save")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = false, dataTypeClass = String.class, paramType = "header", example = ""),
    })
    @ApiOperation(value = "개인정보제공 저장 API", notes = "" +
            "PersonalInfoProvision 저장")
    public ResponseEntity<Map<String, Object>> provisionSave(@RequestBody ProvisionSaveDto provisionSaveDto, HttpServletRequest request){
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return provisionService.provisionSave(provisionSaveDto, jwtFilterDto.getEmail());
    }











}