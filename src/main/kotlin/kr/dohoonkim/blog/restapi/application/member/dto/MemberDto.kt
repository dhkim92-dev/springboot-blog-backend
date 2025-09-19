package kr.dohoonkim.blog.restapi.application.member.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import kr.dohoonkim.blog.restapi.domain.member.Member
import java.time.LocalDateTime
import java.util.UUID

data class MemberDto(
    val id: UUID,
    val nickname: String,
    val email: String,
    val role: String,
    val createdAt: LocalDateTime,
    val isActivated: Boolean
) {

    companion object {

        fun from(member: Member): MemberDto {
            return MemberDto(
                id = member.id!!,
                nickname = member.nickname,
                email = member.email,
                role = member.role.rolename,
                createdAt = member.createdAt,
                isActivated = !member.isBlocked
            );
        }
    }
}
