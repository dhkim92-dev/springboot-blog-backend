package kr.dohoonkim.blog.restapi.common.error.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCode

class UnauthorizedException(errorCode : ErrorCode = ErrorCode.AUTHENTICATION_FAIL) : BusinessException(errorCode){
}