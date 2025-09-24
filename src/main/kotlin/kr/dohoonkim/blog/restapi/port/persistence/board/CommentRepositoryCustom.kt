package kr.dohoonkim.blog.restapi.port.persistence.board

import kr.dohoonkim.blog.restapi.application.board.dto.comment.CommentDto
import kr.dohoonkim.blog.restapi.port.persistence.board.dto.CommentQueryModel
import java.util.UUID

interface CommentRepositoryCustom {

    fun getPostCommentsWithCursor(
        postId: UUID,
        cursor: UUID?,
        size: Int
    ): List<CommentQueryModel>

    fun getCommentRepliesWithCursor(
        commentId: UUID,
        cursor: UUID?,
        size: Int
    ): List<CommentQueryModel>
}