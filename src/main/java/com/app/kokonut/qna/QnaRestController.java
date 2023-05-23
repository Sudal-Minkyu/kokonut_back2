package com.app.kokonut.qna;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.qna.dtos.QnaAnswerSaveDto;
import com.app.kokonut.qna.dtos.QnaQuestionSaveDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v2/api/Qna")
public class QnaRestController {

    private final QnaService qnaService;

    public QnaRestController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @ApiOperation(value="1:1 문의 목록 조회", notes="" +
            "1. 토큰과 페이지 처리를 위한 값을 받는다." +
            "2. QnA 문의 내역을 조회한다")
    @GetMapping(value = "/qnaList") // -> 기존의 코코넛 호출 메서드명 : list - SystemQnaController, MemberQnaController
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> qnaList(@PageableDefault Pageable pageable) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return qnaService.qnaList(jwtFilterDto, pageable);
    }

    @ApiOperation(value="1:1 문의 등록", notes="" +
            "1. QnA 문의를 등록한다.")
    @PostMapping(value = "/qnaWrite", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}) // -> 기존의 코코넛 호출 메서드명 : writeView, save - MemberQnaController
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> qnaWrite(@ModelAttribute QnaQuestionSaveDto qnaQuestionSaveDto) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return qnaService.qnaWrite(qnaQuestionSaveDto, jwtFilterDto);
    }

    @ApiOperation(value="1:1 문의 내용 조회", notes="" +
            "1. 토큰과 조회하고자 하는 QnA 문의 ID를 받는다." +
            "2. QnA 문의 내용을 조회한다.")
    @GetMapping(value = "/qnaDetail/{idx}") // -> 기존의 코코넛 호출 메서드명 : detailView - SystemQnaController, MemberQnaController
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> qnaDetail(@PathVariable("idx") Long qnaId) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return qnaService.qnaDetail(qnaId, jwtFilterDto);
    }

    @ApiOperation(value="1:1 문의 답변 등록", notes="" +
            "1. QnA 답변을 등록한다.")
    @PostMapping(value = "/qnaAnswer") // -> 기존의 코코넛 호출 메서드명 : answer - SystemQnaController
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> qnaAnswer(@RequestBody QnaAnswerSaveDto qnaAnswerSaveDto) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return qnaService.qnaAnswer(qnaAnswerSaveDto, jwtFilterDto);
    }

}
