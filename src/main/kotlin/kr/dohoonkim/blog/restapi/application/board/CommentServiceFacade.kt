package kr.dohoonkim.blog.restapi.application.board

import kr.dohoonkim.blog.restapi.application.board.dto.comment.CreateCommentCommand
import kr.dohoonkim.blog.restapi.application.board.dto.comment.UpdateCommentCommand
import kr.dohoonkim.blog.restapi.application.board.usecases.comment.CreateCommentUseCase
import kr.dohoonkim.blog.restapi.application.board.usecases.comment.DeleteCommentUseCase
import kr.dohoonkim.blog.restapi.application.board.usecases.comment.QueryCommentUseCase
import kr.dohoonkim.blog.restapi.application.board.usecases.comment.UpdateCommentUseCase
import kr.dohoonkim.blog.restapi.common.utility.ContentSanitizer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CommentServiceFacade(
    private val createCommentUseCase: CreateCommentUseCase,
    private val updateCommentUseCase: UpdateCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val queryCommentUseCase: QueryCommentUseCase,
) {

    @Transactional
    fun create(loginId: UUID, command: CreateCommentCommand) = createCommentUseCase.create(loginId, command)

    @Transactional
    fun update(loginId: UUID, command: UpdateCommentCommand) = updateCommentUseCase.update(loginId, command)

    @Transactional
    fun delete(loginId: UUID, commentId: UUID) = deleteCommentUseCase.delete(loginId, commentId)

    @Transactional(readOnly=true)
    fun getPostComments(postId: UUID, cursor: UUID?, size: Int) = queryCommentUseCase.getPostComments(postId, cursor, size)

    @Transactional(readOnly = true)
    fun getCommentReplies(commentId: UUID, cursor: UUID?, size: Int) = queryCommentUseCase.getCommentReply(commentId, cursor, size)
}