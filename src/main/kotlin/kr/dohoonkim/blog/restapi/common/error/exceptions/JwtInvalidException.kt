package kr.dohoonkim.blog.restapi.common.error.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCode

class JwtInvalidException : BusinessException(ErrorCode.INVALID_JWT_EXCEPTION){
}