package com.app.kokonut.revisedDocument;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.revisedDocument.dtos.RevDocSaveDto;
import com.app.kokonut.revisedDocument.dtos.RevDocSearchDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Joy
 * Date : 2023-01-04
 * Time :
 * Remark : 개인정보 처리방침 - 개정문서 컨트롤러
 */
@Validated
@RestController
@RequestMapping("/v2/api/RevisedDocument")
public class RevisedDocumentRestController {
    /* 기존 컨트롤러
     * MemberRevisedDocumentController  "/member/revisedDocument"
     *
     * 변경 컨트롤러
     * RevisedDocumentRestController    "/api/RevisedDocument"
     */
    private final RevisedDocumentService revisedDocumentService;

    public RevisedDocumentRestController(RevisedDocumentService revisedDocumentService) {
        this.revisedDocumentService = revisedDocumentService;
    }
    @ApiOperation(value="개정문서 목록 조회", notes="" +
            "1. 토큰과 페이지처리를 위한 값을 받는다." +
            "2. 처리방침 개정문서 목록을 조회한다.")
    @GetMapping(value = "/revDocList") // -> 기존의 코코넛 호출 메서드명 : list - MemberRevisedDocumentController
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> revDocList(@RequestBody RevDocSearchDto revDocSearchDto, @PageableDefault Pageable pageable) {
        String email = SecurityUtil.getCurrentJwt().getEmail();
        // String userRole = SecurityUtil.getCurrentJwt().getRole();
        return revisedDocumentService.revDocList(null, email, revDocSearchDto, pageable);
    }

    @ApiOperation(value="개정문서 등록", notes="" +
            "1. 처리방침 개정문서를 등록한다.", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(value = "/revDocSave") // -> 기존의 코코넛 호출 메서드명 : save - MemberRevisedDocumentController
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> revDocSave(@Parameter(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
                                                           @Validated RevDocSaveDto revDocDetailDto, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // String userRole = SecurityUtil.getCurrentJwt().getRole();
        String email = SecurityUtil.getCurrentJwt().getEmail();
        return revisedDocumentService.revDocSave(null, email, revDocDetailDto, request, response);
    }
//    @ApiOperation(value="개정문서 삭제", notes="처리방침 개정문서 삭제")
//    @PostMapping(value = "/revDocDelete") // -> 기존의 코코넛 호출 메서드명 : 서비스만 있음. 호출x,  - MemberRevisedDocumentController
//    @ApiImplicitParams({@ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")})
//    public ResponseEntity<Map<String,Object>> revDocDelete(@RequestParam(name="idx") Integer idx) {
//        String userRole = SecurityUtil.getCurrentJwt().getRole();
//        String email = SecurityUtil.getCurrentJwt().getEmail();
//        return revisedDocumentService.revDocDelete(userRole, email, idx);
//    }
}
