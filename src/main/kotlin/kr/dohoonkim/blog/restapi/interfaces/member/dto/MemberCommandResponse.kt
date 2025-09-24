package kr.dohoonkim.blog.restapi.interfaces.member.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.dohoonkim.blog.restapi.application.member.dto.MemberDto
import kr.dohoonkim.blog.restapi.common.response.BaseResponse
import kr.dohoonkim.blog.restapi.domain.member.Role
import java.time.LocalDateTime
import java.util.UUID

@Schema(description = "Member Command Response")
class MemberCommandResponse(
    @Schema(description = "Member ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    val id: UUID,
    @Schema(description = "이메일", example = "")
    val email: String,
    @Schema(description = "닉네임", example = "dohoon")
    val nickname: String,
    @Schema(description = "권한", example = "MEMBER")
    val role: String,
    @Schema(description = "가입 일", example = "2023-10-01T12:00:00")
    val createdAt: LocalDateTime,
): BaseResponse() {

    companion object {
        fun from(memberDto: MemberDto): MemberCommandResponse {
            return MemberCommandResponse(
                id = memberDto.id,
                email = memberDto.email,
                nickname = memberDto.nickname,
                createdAt = memberDto.createdAt,
                role = memberDto.role
            )
        }
    }
}