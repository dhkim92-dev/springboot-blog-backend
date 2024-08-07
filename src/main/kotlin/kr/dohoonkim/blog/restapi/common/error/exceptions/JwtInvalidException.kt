package kr.dohoonkim.blog.restapi.common.error.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCodes.INVALID_JWT_EXCEPTION

class JwtInvalidException() : BusinessException(INVALID_JWT_EXCEPTION) {

}