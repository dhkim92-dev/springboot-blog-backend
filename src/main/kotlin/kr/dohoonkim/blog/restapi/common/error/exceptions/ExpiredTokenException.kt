package kr.dohoonkim.blog.restapi.common.error.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCodes

class ExpiredTokenException:
BusinessException(ErrorCodes.EXPIRED_TOKEN_EXCEPTION) {

}