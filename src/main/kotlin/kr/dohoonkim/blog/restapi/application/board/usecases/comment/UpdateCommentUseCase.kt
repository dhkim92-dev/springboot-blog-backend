package kr.dohoonkim.blog.restapi.application.board.usecases.comment

import kr.dohoonkim.blog.restapi.application.board.dto.comment.UpdateCommentCommand
import java.util.UUID

interface UpdateCommentUseCase {

    fun update(loginId: UUID, command: UpdateCommentCommand)
}