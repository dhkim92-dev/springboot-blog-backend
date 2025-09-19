package kr.dohoonkim.blog.restapi.application.member.dto

import kr.dohoonkim.blog.restapi.domain.member.Member
import java.time.LocalDateTime
import java.util.UUID

class MemberQueryModelDto(
    val id: UUID,
    val email: String,
    val nickname: String,
    val isDeleted: Boolean,
    val role: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {

    companion object {
        fun from(entity: Member): MemberQueryModelDto {
            return MemberQueryModelDto(
                id = entity.id!!,
                email = entity.email,
                nickname = entity.nickname,
                isDeleted = entity.isDeleted,
                role = entity.role.name,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
            )
        }
    }
}