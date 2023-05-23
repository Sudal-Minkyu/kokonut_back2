package com.app.kokonut.totalDBDownload;

import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.totalDBDownload.dtos.TotalDbDownloadSearchDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-01-13
 * Time :
 * Remark : 회원 DB 데이터 다운로드 요청 관련 API RestController
 */
@Slf4j
@RequestMapping("/v2/api/TotalDbDownload")
@RestController
public class TotalDbDownloadRestController {

    private final TotalDbDownloadService totalDbDownloadService;

    @Autowired
    public TotalDbDownloadRestController(TotalDbDownloadService totalDbDownloadService){
        this.totalDbDownloadService = totalDbDownloadService;
    }

    // 개인정보 DB 데이터 전체 다운로드 요청 - 기존코코넛 메서드 : /member/totalDbDownload/apply
    @PostMapping(value = "/userDbDataDownloadApply")
    @ApiImplicitParams({
			@ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey"),
    })
    public ResponseEntity<Map<String, Object>> userDbDataDownloadApply(@RequestParam(name="otpValue", defaultValue = "") String otpValue,
                                                                       @RequestParam(name="reason", defaultValue = "") String reason) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return totalDbDownloadService.userDbDataDownloadApply(otpValue, reason, jwtFilterDto.getEmail());
    }

    // 개인정보 DB 데이터 다운로드 요청건 리스트 - 기존코코넛 메서드 : /member/totalDbDownload/list
    @PostMapping(value = "/userDbDataDownloadList")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey"),
    })
    public ResponseEntity<Map<String, Object>> userDbDataDownloadList(@RequestBody TotalDbDownloadSearchDto totalDbDownloadSearchDto, @PageableDefault Pageable pageable) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return totalDbDownloadService.userDbDataDownloadList(totalDbDownloadSearchDto, jwtFilterDto.getEmail(), pageable);
    }

    // 개인정보 DB 데이터 다운로드 시작 - 기존코코넛 메서드 : /member/totalDbDownload/download
    @PostMapping(value = "/userDbDataDownloadStart")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey"),
    })
    public void userDbDataDownloadStart(@RequestParam(name="tdId", defaultValue = "") Long tdId, HttpServletRequest request, HttpServletResponse response) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        totalDbDownloadService.userDbDataDownloadStart(tdId, jwtFilterDto.getEmail(), request, response);
    }

}
