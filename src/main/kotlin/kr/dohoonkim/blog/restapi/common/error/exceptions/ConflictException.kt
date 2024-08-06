package kr.dohoonkim.blog.restapi.common.error.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CONFLICT

class ConflictException(code: String, message: String)
: BusinessException(CONFLICT, code, message) {

    constructor(errorCode: ErrorCode)
    : this (errorCode.code, errorCode.message) {

    }
}