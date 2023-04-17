package com.app.kokonutapi.history;

import com.app.kokonut.history.HistoryService;
import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-03-29
 * Time :
 * Remark : Kokonut History API RestController
 */
@Slf4j
@RestController
@RequestMapping("/v3/api/History")
public class HistoryApiRestController {

    private final HistoryService historyService;

    @Autowired
    public HistoryApiRestController(HistoryService historyService) {
        this.historyService = historyService;
    }

//    @GetMapping("/activityList")
//    @ApiOperation(value="API용 관리자 활동이력 조회", notes="" +
//            "1. 사용자는 API를 호출한다." +
//            "2. 관리자 활동이력 목록을 조회한다.")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = false, dataTypeClass = String.class, paramType = "header", example = ""),
//            @ApiImplicitParam(name ="ApiKey", value="API Key",required = false, dataTypeClass = String.class, paramType = "header", example = "")
//    })
//    public ResponseEntity<Map<String,Object>> activityList(@RequestParam(value="searchText", defaultValue = "") String searchText,
//                                                   @RequestParam(value="stime", defaultValue = "") String stime,
//                                                   @RequestParam(value="actvityType", defaultValue = "") String actvityType,
//                                                   @PageableDefault Pageable pageable, HttpServletRequest request) {
//        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
//        return historyService.activityList(jwtFilterDto.getEmail(), searchText, stime, actvityType, pageable);
//    }


}
