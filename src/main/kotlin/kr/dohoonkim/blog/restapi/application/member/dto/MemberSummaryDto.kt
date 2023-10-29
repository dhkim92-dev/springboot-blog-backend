package kr.dohoonkim.blog.restapi.application.member.dto

import kr.dohoonkim.blog.restapi.domain.member.Member
import java.util.UUID

data class MemberSummaryDto(
    val id: UUID,
    val nickname: String
) {
    companion object {
        fun fromEntity(member: Member) = MemberSummaryDto(
            id = member.id,
            nickname = member.nickname
        )
    }
}
