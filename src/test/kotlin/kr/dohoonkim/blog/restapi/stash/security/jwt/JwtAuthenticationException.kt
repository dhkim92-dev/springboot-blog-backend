package kr.dohoonkim.blog.restapi.stash.security.jwt

import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import org.springframework.security.core.AuthenticationException

class JwtAuthenticationException(val errorCode : ErrorCode) : AuthenticationException(errorCode.message) {

}