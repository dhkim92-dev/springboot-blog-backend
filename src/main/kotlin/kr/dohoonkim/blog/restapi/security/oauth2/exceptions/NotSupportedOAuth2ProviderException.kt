package kr.dohoonkim.blog.restapi.security.oauth2.exceptions

import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.BadRequestException

class NotSupportedOAuth2ProviderException()
: BadRequestException(ErrorCodes.NOT_SUPPORTED_OAUTH2_PROVIDER) {
}