package kr.dohoonkim.blog.restapi.common.error.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import org.springframework.http.HttpStatus.BAD_REQUEST

open class BadRequestException(
    _code: String,
    _message: String
): BusinessException(BAD_REQUEST, _code, _message) {

    constructor(errorCode: ErrorCode)
    : this(errorCode.code, errorCode.message) {}

    constructor(message: String)
    : this(ErrorCodes.BAD_REQUEST.code, message) {}
}