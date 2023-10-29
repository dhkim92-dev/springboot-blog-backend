package kr.dohoonkim.blog.restapi.common.error.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.ErrorResponse
import java.lang.RuntimeException


open class BusinessException : RuntimeException {
    var errorCode: ErrorCode
    var errors: List<ErrorResponse.FieldError> = arrayListOf<ErrorResponse.FieldError>()

    public constructor(message: String, errorCode: ErrorCode) : super(message) {
        this.errorCode = errorCode
    }

    public constructor(errorCode: ErrorCode) : super(errorCode.message) {
        this.errorCode = errorCode
    }

    public constructor(errorCode: ErrorCode, errors: List<ErrorResponse.FieldError>)
            : super(errorCode.message) {
        this.errors = errors;
        this.errorCode = errorCode;
    }
}