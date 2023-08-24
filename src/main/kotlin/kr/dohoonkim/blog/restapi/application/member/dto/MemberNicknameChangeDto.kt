package kr.dohoonkim.blog.restapi.application.member.dto

import java.util.UUID

data class MemberNicknameChangeDto(
    val memberId : UUID,
    val nickname : String
)
