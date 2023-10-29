package kr.dohoonkim.blog.restapi.application.member.dto

import java.util.UUID

data class MemberPasswordChangeDto(
    val memberId: UUID,
    val currentPassword: String,
    val newPassword: String
)
