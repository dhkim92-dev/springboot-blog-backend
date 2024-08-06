package kr.dohoonkim.blog.restapi.common.error.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCodes

class JwtInvalidException : BusinessException(ErrorCodes.INVALID_JWT_EXCEPTION) {
}