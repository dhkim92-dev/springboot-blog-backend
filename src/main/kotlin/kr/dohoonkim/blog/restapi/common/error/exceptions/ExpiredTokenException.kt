package kr.dohoonkim.blog.restapi.common.error.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCode

class ExpiredTokenException : BusinessException(ErrorCode.EXPIRED_TOKEN_EXCEPTION){
}