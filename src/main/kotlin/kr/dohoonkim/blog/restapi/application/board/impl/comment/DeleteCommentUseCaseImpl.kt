package kr.dohoonkim.blog.restapi.application.board.impl.comment

import kr.dohoonkim.blog.restapi.application.board.usecases.comment.DeleteCommentUseCase
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.port.persistence.board.CommentRepository
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class DeleteCommentUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val commentRepository: CommentRepository
): DeleteCommentUseCase {

    override fun delete(loginId: UUID, commentId: UUID) {
        val member = memberRepository.findByIdOrNull(loginId)
            ?: throw ForbiddenException(ErrorCodes.NO_PERMISSION)
        val comment = commentRepository.findByIdOrNull(commentId)
            ?: throw NotFoundException(ErrorCodes.COMMENT_NOT_FOUND)

        if ( comment.writer.id != member.id ) {
            throw ForbiddenException(ErrorCodes.NO_PERMISSION)
        }
        comment.markAsDeleted()
        commentRepository.save(comment)
    }
}