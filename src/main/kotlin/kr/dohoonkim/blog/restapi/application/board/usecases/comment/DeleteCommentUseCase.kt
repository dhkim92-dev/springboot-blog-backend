package kr.dohoonkim.blog.restapi.application.board.usecases.comment

import java.util.UUID

interface DeleteCommentUseCase {

    fun delete(loginId: UUID, commentId: UUID)
}