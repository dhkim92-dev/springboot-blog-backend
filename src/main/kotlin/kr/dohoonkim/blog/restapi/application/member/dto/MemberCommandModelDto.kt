package kr.dohoonkim.blog.restapi.application.member.dto

import java.time.LocalDateTime
import java.util.UUID

class MemberCommandModelDto(
    val id: UUID,
    val nickname: String,
    val email: String,
    val createdAt: LocalDateTime
) {
}