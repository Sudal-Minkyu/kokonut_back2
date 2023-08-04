package com.app.kokonut.common.exceptionhandle;

import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.configs.exception.KokonutAPIException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * @author Woody
 * Date : 2023-05-18
 *
 * Time :
 * Remark : 글로벌 예외처리 핸들러
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // NumberFormatException 예외처리
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleNumberFormatException(NumberFormatException e) {
        AjaxResponse res = new AjaxResponse();
        log.error("에러내용 : "+ResponseErrorCode.ERROR_CODE_01.getDesc()+" 코드 : "+e.getMessage());
        return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_01.getCode(),ResponseErrorCode.ERROR_CODE_01.getDesc()+" 코드 : "+e.getMessage()));
    }

    // 현재는 코코넛API 호출할때만 에러를 호출하는 핸들러
    @ExceptionHandler(KokonutAPIException.class)
    public ResponseEntity<Map<String, Object>> handleKokonutAPIException(KokonutAPIException e) {
        AjaxResponse res = new AjaxResponse();
        log.error("에러내용 : "+e.getErrorCode().getDesc());
        return ResponseEntity.ok(res.fail(e.getErrorCode().getCode(),e.getErrorCode().getDesc()));
    }

    // 무슨에러가 발생했는지 모를때 -> 일단주석처리
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, Object>> handleKokonutException(){
//        AjaxResponse res = new AjaxResponse();
//        log.error("에러내용 : "+ResponseErrorCode.ERROR_KOKONUT.getDesc());
//        return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_KOKONUT.getCode(),ResponseErrorCode.ERROR_KOKONUT.getDesc()));
//    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleAccessDeniedException(AccessDeniedException ex) {
        log.error("에러내용 : 403 에러발생!");
        log.error("ex : "+ex);
        log.error("ex.getMessage() : "+ex.getMessage());
    }

}