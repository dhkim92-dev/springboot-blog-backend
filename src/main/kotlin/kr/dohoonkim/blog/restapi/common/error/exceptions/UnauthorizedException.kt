package kr.dohoonkim.blog.restapi.common.error.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import org.springframework.http.HttpStatus

class UnauthorizedException(
    code: String,
    message: String
) : BusinessException(HttpStatus.UNAUTHORIZED, code, message) {

    constructor(errorCode: ErrorCode)
    : this(errorCode.code, errorCode.message) {

    }
}