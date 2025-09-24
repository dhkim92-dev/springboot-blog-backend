package kr.dohoonkim.blog.restapi.application.board.usecases.article

import kr.dohoonkim.blog.restapi.application.board.dto.article.UpdatePostCommand
import java.util.UUID

interface UpdatePostUseCase {

    /**
     * 게시물 수정
     * @param loginId 게시물을 수정하는 회원의 UUID
     * @param command 게시물 수정에 필요한 정보
     */
    fun update(loginId: UUID, command: UpdatePostCommand)
}