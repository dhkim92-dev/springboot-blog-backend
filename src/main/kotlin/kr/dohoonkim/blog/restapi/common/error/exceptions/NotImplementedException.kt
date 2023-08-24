package kr.dohoonkim.blog.restapi.common.error.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCode

class NotImplementedException : BusinessException(ErrorCode.NOT_IMPLEMENTED){
}