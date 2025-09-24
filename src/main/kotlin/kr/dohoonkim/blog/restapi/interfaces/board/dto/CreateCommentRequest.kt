package kr.dohoonkim.blog.restapi.interfaces.board.dto

import kr.dohoonkim.blog.restapi.application.board.dto.comment.CreateCommentCommand
import kr.dohoonkim.blog.restapi.application.board.dto.comment.UpdateCommentCommand
import java.util.UUID

class CreateCommentRequest(
    val content: String
) {
    fun toCommand(postId: UUID, parentId: UUID?) = CreateCommentCommand(
        postId = postId,
        parentId = parentId,
        content = content
    )

    fun toCommand(commentId: UUID): UpdateCommentCommand {
        return UpdateCommentCommand(
            commentId = commentId,
            content = content
        )
    }
}