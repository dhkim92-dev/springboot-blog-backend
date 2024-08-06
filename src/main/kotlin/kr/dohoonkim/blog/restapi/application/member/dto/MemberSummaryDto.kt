package kr.dohoonkim.blog.restapi.application.member.dto

import jakarta.validation.constraints.NotBlank
import kr.dohoonkim.blog.restapi.domain.member.Member
import java.util.UUID

/**
 * Member 정보 요약 객체
 * @property id 사용자 ID
 * @property nickname 사용자 닉네임
 */
data class MemberSummaryDto(
    val id: UUID,
    val nickname: String
) {
    companion object {

        fun fromEntity(member: Member): MemberSummaryDto {
            return MemberSummaryDto(
                id = member.id,
                nickname = member.nickname
            );
        }
    }
}
