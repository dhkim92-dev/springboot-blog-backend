package kr.dohoonkim.blog.restapi.application.board.usecases.article

import java.util.UUID

interface DeletePostUseCase {

    /**
     * 게시글 삭제
     * @param loginId 로그인한 사용자 ID
     * @param postId 삭제할 게시글 ID
     */
    fun delete(loginId: UUID, postId: UUID)
}