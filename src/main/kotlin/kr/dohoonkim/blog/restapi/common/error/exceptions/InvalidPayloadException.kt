package kr.dohoonkim.blog.restapi.common.error.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCode

class InvalidPayloadException : BusinessException(ErrorCode.INVALID_PAYLOAD_EXCEPTION) {

}