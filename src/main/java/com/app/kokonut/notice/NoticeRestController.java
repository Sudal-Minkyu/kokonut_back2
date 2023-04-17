package com.app.kokonut.notice;

import org.springframework.data.web.PageableDefault;
import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.notice.dtos.NoticeDetailDto;
import com.app.kokonut.notice.dtos.NoticeSearchDto;
import com.app.kokonut.notice.dtos.NoticeStateDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Joy
 * Date : 2022-12-27
 * Time :
 * Remark : 공지사항 컨트롤러
 */
@Api(tags = "")
@Validated
@RestController
@RequestMapping("/v2/api/Notice")
public class NoticeRestController {
    /* 기존 컨트롤러
     * SystemNoticeController  "/system/notice"
     * NoticeController  "/notice"
     *
     * 변경 컨트롤러
     * NoticeRestController    "/api/Notice"
     */

    private final NoticeService noticeService;

    public NoticeRestController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }
    @ApiOperation(value="Notice 목록 조회", notes="" +
            "1. 토큰과 페이지 처리를 위한 값을 받는다." +
            "2. 공지사항 목록을 조회한다.")
    @GetMapping(value = "/noticeList") // -> 기존의 코코넛 호출 메서드명 : getList, list - NoticeController
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> noticeList(@RequestBody NoticeSearchDto noticeSearchDto, @PageableDefault Pageable pageable) {
//        String userRole = SecurityUtil.getCurrentJwt().getRole();
        return noticeService.noticeList(null, noticeSearchDto, pageable);
    }

    @ApiOperation(value="Notice 내용 조회", notes="" +
            "1. 토큰과 조회하고자 하는 공지사항 ID를 받는다." +
            "2. 공지사항 내용을 조회한다.")
    @GetMapping(value = "/noticeDetail/{ntId}") // -> 기존의 코코넛 호출 메서드명 : detailView - SystemNoticeController, NoticeController
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> noticeDetail(@PathVariable("ntId") Long ntId) {
//        String userRole = SecurityUtil.getCurrentJwt().getRole();
        return noticeService.noticeDetail(null, ntId);
    }

    @ApiOperation(value="Notice 등록, 수정", notes="" +
            "1. 공지사항 수정, 등록")
    @PostMapping(value = "/noticeSave") // -> 기존의 코코넛 호출 메서드명 : save - SystemNoticeController
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> noticeSave(@RequestBody NoticeDetailDto noticeDetailDto) {
//        String userRole = SecurityUtil.getCurrentJwt().getRole();
        String email = SecurityUtil.getCurrentJwt().getEmail();
        return noticeService.noticeSave(null, email, noticeDetailDto);
    }

    @ApiOperation(value="Notice 삭제", notes="" +
            "1. 토큰과 삭제하고자 하는 공지사항 ID를 받는다." +
            "2. 공지사항을 삭제한다.")
    @PostMapping(value = "/noticeDelete") // -> 기존의 코코넛 호출 메서드명 : delete - SystemNoticeController
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> noticeDelete(@RequestParam(name="ntId") Long ntId) {
//        String userRole = SecurityUtil.getCurrentJwt().getRole();
        String email = SecurityUtil.getCurrentJwt().getEmail();
        return noticeService.noticeDelete(null, email, ntId);
    }

    @ApiOperation(value="Notice 게시 상태변경", notes="" +
            "1. 공지사항 게시 상태를 변경한다.")
    @PostMapping(value = "/noticeState") // -> 기존의 코코넛 호출 메서드명 : updatePostingState, updateStopState - SystemNoticeController
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> noticeState(@RequestBody NoticeStateDto noticeStateDto) {
//        String userRole = SecurityUtil.getCurrentJwt().getRole();
        String email = SecurityUtil.getCurrentJwt().getEmail();
        return noticeService.noticeState(null, email, noticeStateDto);
    }
}
