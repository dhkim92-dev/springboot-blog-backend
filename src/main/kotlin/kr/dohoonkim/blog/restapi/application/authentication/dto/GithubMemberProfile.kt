package kr.dohoonkim.blog.restapi.application.authentication.dto

import kr.dohoonkim.blog.restapi.common.error.ErrorCode.OAUTH2_NOT_VERIFIED_EMAIL
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnauthorizedException

class GithubMemberProfile(attributes: MutableMap<String, Any>) : MemberProfile(attributes) {
    init {
        this.nickname = "github:${attributes["id"]}"
        this.email = "${nickname}@github.com"
    }
}