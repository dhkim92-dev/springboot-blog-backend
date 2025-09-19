package kr.dohoonkim.blog.restapi.interfaces.member.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.dohoonkim.blog.restapi.application.member.dto.MemberQueryModelDto
import java.time.LocalDateTime
import java.util.UUID

@Schema(description = "멤버 조회 응답")
class MemberQueryResponse(
    val id: UUID,
    val email: String,
    val nickname: String,
    val isDeleted: Boolean,
    val role: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {

    companion object {
        fun from(dto: MemberQueryModelDto): MemberQueryResponse {
            return MemberQueryResponse(
                id = dto.id,
                email = dto.email,
                nickname = dto.nickname,
                isDeleted = dto.isDeleted,
                role = dto.role,
                createdAt = dto.createdAt,
                updatedAt = dto.updatedAt,
            )
        }
    }
}