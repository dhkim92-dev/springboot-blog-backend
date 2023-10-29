package kr.dohoonkim.blog.restapi.application.member.dto

import java.util.*

data class MemberEmailChangeDto(
    val memberId: UUID,
    val email: String
)
