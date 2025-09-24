package kr.dohoonkim.blog.restapi.application.member.dto

import kr.dohoonkim.blog.restapi.domain.member.Member
import java.util.UUID

data class MemberSummaryDto(
    val id: UUID = UUID.randomUUID(),
    val nickname: String = "탈퇴한 회원"
) {

    companion object {
        fun from(entity: Member): MemberSummaryDto {
            return MemberSummaryDto(
                id = entity.id!!,
                nickname = entity.nickname
            )
        }
    }
}