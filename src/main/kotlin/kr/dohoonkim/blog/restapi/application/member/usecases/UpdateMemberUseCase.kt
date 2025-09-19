package kr.dohoonkim.blog.restapi.application.member.usecases

import kr.dohoonkim.blog.restapi.application.member.dto.UpdateMemberCommand
import java.util.UUID

interface UpdateMemberUseCase {

    /**
     * 멤버의 정보를 업데이트합니다.
     * @param memberId 현재 로그인한 멤버의 ID
     * @param command 업데이트할 멤버 정보
     * @return 업데이트된 멤버 정보
     */
    fun update(memberId: UUID, command: UpdateMemberCommand)
}