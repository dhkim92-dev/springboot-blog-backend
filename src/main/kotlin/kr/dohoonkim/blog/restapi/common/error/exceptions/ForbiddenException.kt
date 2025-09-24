package kr.dohoonkim.blog.restapi.common.error.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import org.springframework.http.HttpStatus.FORBIDDEN

class ForbiddenException(
    code: String,
    message: String
) : BusinessException(FORBIDDEN, code, message) {

    constructor(errorCode: ErrorCode)
    : this(errorCode.code, errorCode.message) {

    }

    constructor(message: String): this(ErrorCodes.NO_PERMISSION.code, message) {

    }
}