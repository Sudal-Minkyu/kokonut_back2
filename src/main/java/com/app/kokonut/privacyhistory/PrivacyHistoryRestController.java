package com.app.kokonut.privacyhistory;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.history.HistoryService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * @author Woody
 * Date : 2022-11-03
 * Time :
 * Remark : Kokonut ActivityHistory RestController
 */
@Slf4j
@RestController
@RequestMapping("/v2/api/PrivacyHistory")
public class PrivacyHistoryRestController {

    private final PrivacyHistoryService privacyHistoryService;

    @Autowired
    public PrivacyHistoryRestController(PrivacyHistoryService privacyHistoryService) {
        this.privacyHistoryService = privacyHistoryService;
    }

    @GetMapping("/privacyHistoryList")
    @ApiOperation(value="개인정보 처리 활동이력 조회", notes="")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> privacyHistoryList(@RequestParam(value="searchText", defaultValue = "") String searchText,
                                                                 @RequestParam(value="stime", defaultValue = "") String stime,
                                                                 @RequestParam(value="filterRole", defaultValue = "") String filterRole,
                                                                 @RequestParam(value="filterState", defaultValue = "") String filterState,
                                                                 @PageableDefault Pageable pageable) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return privacyHistoryService.privacyHistoryList(searchText, stime, filterRole, filterState, jwtFilterDto,  pageable);
    }

    @PostMapping("/privacyHistoryDownloadExcel")
    @ApiOperation(value="개인정보 처리 활동이력 엑셀다운로드", notes="")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> privacyHistoryDownloadExcel(@RequestParam(value="searchText", defaultValue = "") String searchText,
                                                                 @RequestParam(value="stime", defaultValue = "") String stime,
                                                                 @RequestParam(value="filterRole", defaultValue = "") String filterRole,
                                                                 @RequestParam(value="filterState", defaultValue = "") String filterState,
                                                                 @RequestParam(value="otpValue", defaultValue = "") String otpValue,
                                                                 @RequestParam(value="downloadReason", defaultValue = "") String downloadReason) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return privacyHistoryService.privacyHistoryDownloadExcel(searchText, stime, filterRole, filterState, otpValue, downloadReason, jwtFilterDto);
    }



}
