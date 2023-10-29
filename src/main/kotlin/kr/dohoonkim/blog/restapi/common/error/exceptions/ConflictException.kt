package kr.dohoonkim.blog.restapi.common.error.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCode

class ConflictException(errorCode: ErrorCode) : BusinessException(errorCode) {
}