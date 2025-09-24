package kr.dohoonkim.blog.restapi.application.member.dto

import kr.dohoonkim.blog.restapi.domain.member.OAuth2Provider

class CreateOAuth2MemberCommand(
    val provider: OAuth2Provider,
    val userId: String,
    val email: String,
    val nickname: String,
) {

}