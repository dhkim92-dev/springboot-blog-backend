package kr.dohoonkim.blog.restapi.application.file.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.exceptions.BusinessException

class NotSupportImageTypeException : BusinessException(ErrorCode.NOT_SUPPORT_IMAGE_TYPE) {

}