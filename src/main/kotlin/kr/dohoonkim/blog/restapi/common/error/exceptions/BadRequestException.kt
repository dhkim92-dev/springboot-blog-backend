package kr.dohoonkim.blog.restapi.common.error.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCode

open class BadRequestException(errorCode: ErrorCode) : BusinessException(errorCode) {
}