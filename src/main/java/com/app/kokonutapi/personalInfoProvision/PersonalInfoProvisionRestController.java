package com.app.kokonutapi.personalInfoProvision;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonutapi.personalInfoProvision.dtos.PersonalInfoProvisionSaveDto;
import com.app.kokonutapi.personalInfoProvision.dtos.PersonalInfoProvisionSetDto;
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
@RequestMapping("/v3/api/PersonalInfoProvision")
public class PersonalInfoProvisionRestController {

    private final PersonalInfoProvisionService personalInfoProvisionService;

    @Autowired
    public PersonalInfoProvisionRestController(PersonalInfoProvisionService personalInfoProvisionService){
        this.personalInfoProvisionService = personalInfoProvisionService;
    }

    // 기존 코코넛 메서드 : provisionList
    @PostMapping("/save")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = false, dataTypeClass = String.class, paramType = "header", example = ""),
            @ApiImplicitParam(name ="ApiKey", value="API Key",required = false, dataTypeClass = String.class, paramType = "header", example = "")
    })
    @ApiOperation(value = "정보제공 저장 API", notes = "" +
            "PersonalInfoProvision 저장")
    public ResponseEntity<Map<String, Object>> provisionSave(@RequestBody PersonalInfoProvisionSaveDto personalInfoProvisionSaveDto, HttpServletRequest request){
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return personalInfoProvisionService.privisionSave(personalInfoProvisionSaveDto, jwtFilterDto.getEmail());
    }

    // 기존 코코넛 메서드 : provisionList
    @GetMapping("/list")
    @ApiOperation(value = "정보제공 목록 조회 API", notes = "" +
            "PersonalInfoProvision 리스트를 조회한다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = false, dataTypeClass = String.class, paramType = "header", example = "jwtKey"),
            @ApiImplicitParam(name ="ApiKey", value="API Key",required = false, dataTypeClass = String.class, paramType = "header", example = "apiKey")
    })
    public ResponseEntity<Map<String,Object>> personalInfoProvisionList(@RequestBody PersonalInfoProvisionSetDto personalInfoProvisionSetDto){
        return personalInfoProvisionService.personalInfoProvisionList(personalInfoProvisionSetDto);
    }


//    @RequestMapping(value = "/provision/list/for-agree", method = RequestMethod.POST)
//    @ResponseBody
    @PostMapping("/for-agree")
    @ApiOperation(value = "잘 모르겠음. 주석안되있는 API", notes = "" +
            " * 상태 \n" +
            " * 1: 수집중: 정보제공일 이전\n" +
            " * 2: 수집완료: 정보제공일 부터 정보제공 만료일 까지\n" +
            " * 3: 기간만료: 정보제공 만료일 이후\n" +
            " */")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = false, dataTypeClass = String.class, paramType = "header", example = "jwtKey"),
            @ApiImplicitParam(name ="ApiKey", value="API Key",required = false, dataTypeClass = String.class, paramType = "header", example = "apiKey")
    })
    public ResponseEntity<Map<String,Object>> provisionListForAgree(@RequestBody PersonalInfoProvisionSetDto personalInfoProvisionSetDto) {
        return personalInfoProvisionService.provisionListForAgree(personalInfoProvisionSetDto);
    }













}