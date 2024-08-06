package kr.dohoonkim.blog.restapi.common.error.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import org.springframework.http.HttpStatus

class NotFoundException(
    _code: String,
    _message: String
): BusinessException(HttpStatus.NOT_FOUND, _code, _message) {

    constructor(errorCode: ErrorCode)
    : this(errorCode.code, errorCode.message) {

    }
}