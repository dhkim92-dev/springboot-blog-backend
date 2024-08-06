package kr.dohoonkim.blog.restapi.common.error.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE

class UnsupportedMediaTypeException(errorCode: ErrorCode)
: BusinessException(
    UNSUPPORTED_MEDIA_TYPE,
    errorCode.code,
    errorCode.message
) {

}