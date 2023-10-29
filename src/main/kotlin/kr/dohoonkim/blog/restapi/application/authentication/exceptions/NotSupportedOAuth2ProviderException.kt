package kr.dohoonkim.blog.restapi.application.authentication.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.exceptions.BadRequestException

class NotSupportedOAuth2ProviderException : BadRequestException(ErrorCode.NOT_SUPPORTED_OAUTH2_PROVIDER) {
}