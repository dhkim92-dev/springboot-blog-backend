package kr.dohoonkim.blog.restapi.application.authentication.dto

import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnauthorizedException

/**
 * Google Member Profile
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 */
class GoogleMemberProfile(attributes: MutableMap<String, Any>) : MemberProfile(attributes) {

    init {
        val isEmailVerified: Boolean = attributes["email_verified"] as Boolean

        if (!isEmailVerified) {
            throw UnauthorizedException(ErrorCode.OAUTH2_NOT_VERIFIED_EMAIL)
        }

        nickname = "google:${attributes["sub"].toString()}"//attributes["name"].toString()
        email = "${this.nickname}@gmail.com"//attributes["email"].toString()
    }
}