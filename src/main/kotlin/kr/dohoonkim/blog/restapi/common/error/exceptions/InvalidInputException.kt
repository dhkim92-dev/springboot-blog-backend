package kr.dohoonkim.blog.restapi.common.error.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.ErrorResponse

class InvalidInputException(errors : List<ErrorResponse.FieldError>) : BusinessException(ErrorCode.INVALID_INPUT_VALUE, errors){
}