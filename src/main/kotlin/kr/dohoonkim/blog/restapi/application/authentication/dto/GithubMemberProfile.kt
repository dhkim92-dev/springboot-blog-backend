package kr.dohoonkim.blog.restapi.application.authentication.dto

import kr.dohoonkim.blog.restapi.common.error.ErrorCode.OAUTH2_NOT_VERIFIED_EMAIL
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnauthorizedException

/**
 * Github Member Profile
 * attributes가 제공되면 nickname 과 email을 생성한다
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 */
class GithubMemberProfile(attributes: MutableMap<String, Any>) : MemberProfile(attributes) {

    init {
        this.nickname = "github:${attributes["id"]}"
        this.email = "${nickname}@github.com"
    }
}