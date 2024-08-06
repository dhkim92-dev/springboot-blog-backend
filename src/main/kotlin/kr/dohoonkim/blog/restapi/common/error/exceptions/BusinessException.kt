package kr.dohoonkim.blog.restapi.common.error.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import org.springframework.http.HttpStatus
import java.lang.RuntimeException


open class BusinessException(
    override val status: HttpStatus,
    override val code: String,
    override val message: String
) : RuntimeException(message), ErrorCode {

    constructor(errorCode: ErrorCode)
    :this(errorCode.status, errorCode.code, errorCode.message) {

    }
}