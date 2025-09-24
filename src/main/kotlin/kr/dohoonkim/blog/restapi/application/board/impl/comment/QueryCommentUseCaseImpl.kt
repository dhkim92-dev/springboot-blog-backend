package kr.dohoonkim.blog.restapi.application.board.impl.comment

import kr.dohoonkim.blog.restapi.application.board.dto.comment.CommentDto
import kr.dohoonkim.blog.restapi.application.board.usecases.comment.QueryCommentUseCase
import kr.dohoonkim.blog.restapi.port.persistence.board.CommentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class QueryCommentUseCaseImpl(
    private val commentRepository: CommentRepository
): QueryCommentUseCase {

    override fun getPostComments(
        postId: UUID,
        cursor: UUID?,
        size: Int
    ): List<CommentDto> {
        val comments = commentRepository.getPostCommentsWithCursor(postId, cursor, size)
        return comments.map { CommentDto.from(it) }
    }

    override fun getCommentReply(commentId: UUID, cursor: UUID?, size: Int): List<CommentDto> {
        val replies = commentRepository.getCommentRepliesWithCursor(commentId, cursor, size)
        return replies.map { CommentDto.from(it) }
    }
}