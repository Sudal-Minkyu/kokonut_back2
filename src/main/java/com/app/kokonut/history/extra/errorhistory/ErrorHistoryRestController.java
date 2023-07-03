package com.app.kokonut.history.extra.errorhistory;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.history.extra.errorhistory.dtos.ErrorHistorySaveDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Woody
 * Date : 2022-06-30
 * Time :
 * Remark :
 */
@Slf4j
@RestController
@RequestMapping("/v2/api/Error")
public class ErrorHistoryRestController {

    private final ErrorHistoryService errorHistoryService;

    @Autowired
    public ErrorHistoryRestController(ErrorHistoryService errorHistoryService) {
        this.errorHistoryService = errorHistoryService;
    }

    @PostMapping(value = "/errorSave")
    @ApiOperation(value = "에러메세지 저장" , notes = "에러메세지를 저장한다")
    @ApiImplicitParam(name ="Authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> errorSave(@RequestBody ErrorHistorySaveDto errorHistorySaveDto) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return errorHistoryService.errorSave(errorHistorySaveDto, jwtFilterDto);
    }


}
