package kr.dohoonkim.blog.restapi.application.board.usecases.comment

import kr.dohoonkim.blog.restapi.application.board.dto.comment.CommentDto
import java.util.UUID

interface QueryCommentUseCase {

    fun getPostComments(
        postId: UUID,
        cursor: UUID?,
        size: Int
    ): List<CommentDto>

    fun getCommentReply(
        commentId: UUID,
        cursor: UUID?,
        size: Int
    ): List<CommentDto>
}