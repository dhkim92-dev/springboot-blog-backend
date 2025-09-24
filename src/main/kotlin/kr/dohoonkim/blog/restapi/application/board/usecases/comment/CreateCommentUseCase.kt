package kr.dohoonkim.blog.restapi.application.board.usecases.comment

import kr.dohoonkim.blog.restapi.application.board.dto.comment.CommentDto
import kr.dohoonkim.blog.restapi.application.board.dto.comment.CreateCommentCommand
import java.util.UUID

interface CreateCommentUseCase {

    fun create(loginId: UUID, command: CreateCommentCommand): CommentDto
}