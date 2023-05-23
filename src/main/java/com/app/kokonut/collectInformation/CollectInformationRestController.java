package com.app.kokonut.collectInformation;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.collectInformation.dtos.CollectInfoDetailDto;
import com.app.kokonut.collectInformation.dtos.CollectInfoSearchDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.springframework.data.web.PageableDefault;

/**
 * @author Joy
 * Date : 2023-01-04
 * Time :
 * Remark : 개인정보 처리방침 - 개인정보 수집 및 이용 안내 컨트롤러
 */
@Validated
@RestController
@RequestMapping("v2/api/CollectInfomation")
public class CollectInformationRestController {
    /* 기존 컨트롤러
     * MemberCollectInformationControlle  "/member/collectInformation"
     *
     * 변경 컨트롤러
     * CollectInformationRestController    "/api/CollectInfomation"
     */

    private final CollectInformationService collectInformationService;

    public CollectInformationRestController(CollectInformationService collectInformationService) {
        this.collectInformationService = collectInformationService;
    }
    @ApiOperation(value="개인정보처리방침 목록 조회", notes=""+
            "1. 등록된 개인정보 처리방침 목록을 조회한다.")
    @GetMapping(value = "/collectInfoList") // -> 기존의 코코넛 호출 메서드명 : list - MemberCollectInformationControlle
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header")
    })
    public ResponseEntity<Map<String,Object>> collectInfoList(@RequestBody CollectInfoSearchDto collectInfoSearchDto, @PageableDefault Pageable pageable) {
//        String userRole = SecurityUtil.getCurrentJwt().getRole();
        String email = SecurityUtil.getCurrentJwt().getEmail();
        return collectInformationService.collectInfoList(null, email, collectInfoSearchDto, pageable);
    }

    @ApiOperation(value="개인정보처리방침 내용 조회", notes=""+
            "1. 조회하고자 하는 개인정보 처리방침 아이디와 토큰을 받는다." +
            "2. 등록된 개인정보 처리방침 내용을 조회한다.")
    @GetMapping(value = "/collectInfoDetail") // -> 기존의 코코넛 호출 메서드명 : previewPopup - MemberCollectInformationControlle
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header")
    })
    public ResponseEntity<Map<String,Object>> collectInfoDetail(@RequestParam(name="ciId") Long ciId) {
        // String userRole = SecurityUtil.getCurrentJwt().getRole();
        return collectInformationService.collectInfoDetail(null, ciId);
    }

    @ApiOperation(value="개인정보처리방침 수정, 등록", notes=""+
            "1. 개인정보 처리방침을 받는다." +
            "2. 개인정보 처리방침을 등록, 수정한다.")
    @PostMapping(value = "/collectInfoSave") // -> 기존의 코코넛 호출 메서드명 : save - MemberCollectInformationControlle
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header")
    })
    public ResponseEntity<Map<String,Object>> collectInfoSave(@RequestBody CollectInfoDetailDto collectInfoDetailDto) {
        // String userRole = SecurityUtil.getCurrentJwt().getRole();
        String email = SecurityUtil.getCurrentJwt().getEmail();
        return collectInformationService.collectInfoSave(null, email, collectInfoDetailDto);
    }

    @ApiOperation(value="개인정보처리방침 삭제", notes=""+
            "1. 삭제하고자 하는 개인정보 처리방침 아이디와 토큰을 받는다. " +
            "2. 개인정보 처리방침을 삭제한다.")
    @PostMapping(value = "/collectInfoDelete") // -> 기존의 코코넛 호출 메서드명 : delete - MemberCollectInformationControlle
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header")
    })
    public ResponseEntity<Map<String,Object>> collectInfoDelete(@RequestParam(name="ciId") Long ciId) {
        // String userRole = SecurityUtil.getCurrentJwt().getRole();
        String email = SecurityUtil.getCurrentJwt().getEmail();
        return collectInformationService.collectInfoDelete(null, email, ciId);
    }

}
