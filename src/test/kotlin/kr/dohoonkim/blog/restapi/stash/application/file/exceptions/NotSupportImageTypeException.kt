package kr.dohoonkim.blog.restapi.stash.application.file.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.BusinessException

class NotSupportImageTypeException : BusinessException(ErrorCodes.NOT_SUPPORT_IMAGE_TYPE) {

}