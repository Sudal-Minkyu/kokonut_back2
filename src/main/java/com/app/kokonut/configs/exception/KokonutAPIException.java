package com.app.kokonut.configs.exception;

import com.app.kokonut.common.ResponseErrorCode;

/**
 * @author Woody
 * Date : 2023-07-27
 * Time :
 * Remark : 코코넛 예외처리
 */
public class KokonutAPIException extends RuntimeException {

    private final ResponseErrorCode errorCode;

    public KokonutAPIException(ResponseErrorCode errorCode) {
        super(errorCode.getDesc());
        this.errorCode = errorCode;
    }

    public ResponseErrorCode getErrorCode() {
        return this.errorCode;
    }

}