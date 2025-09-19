package kr.dohoonkim.blog.restapi.application.member.usecases

import java.util.UUID

interface DeleteMemberUseCase {

    /**
     * 회원 탈퇴
     * @param memberId 요청을 보낸 회원 ID
     * @param targetId 탈퇴 대상 회원 ID
     */
    fun deleteMember(memberId: UUID, targetId: UUID)
}