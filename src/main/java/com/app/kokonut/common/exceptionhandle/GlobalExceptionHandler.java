package com.app.kokonut.common.exceptionhandle;

import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
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
        log.error("메뉴얼에 지정되지 않은 값입니다. 고유 코드를 확인해 주시고 호출해 주시길 바랍니다. 코드 : "+e.getMessage());
        return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_01.getCode(),ResponseErrorCode.ERROR_CODE_01.getDesc()+" 코드 : "+e.getMessage()));
    }

}