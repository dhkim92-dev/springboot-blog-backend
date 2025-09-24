package kr.dohoonkim.blog.restapi.application.member.dto

import java.util.UUID

/**
 * 회원 정보 수정을 위한 커맨드 객체
 * @param targetId 수정할 회원의 ID
 * @param nickname 새로운 닉네임
 */
class UpdateMemberCommand(
    val targetId: UUID,
    val nickname: String,
) {
}