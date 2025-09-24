package kr.dohoonkim.blog.restapi.application.board.impl.comment

import kr.dohoonkim.blog.restapi.application.board.dto.comment.UpdateCommentCommand
import kr.dohoonkim.blog.restapi.application.board.usecases.comment.UpdateCommentUseCase
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.common.utility.ContentSanitizer
import kr.dohoonkim.blog.restapi.port.persistence.board.CommentRepository
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class UpdateCommentUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val commentRepository: CommentRepository,
    private val contentSanitizer: ContentSanitizer
): UpdateCommentUseCase {

    override fun update(loginId: UUID, command: UpdateCommentCommand) {
        val member = memberRepository.findByIdOrNull(loginId)
            ?: throw ForbiddenException(ErrorCodes.MEMBER_NOT_FOUND)
        val comment = commentRepository.findByIdOrNull(command.commentId)
            ?:  throw NotFoundException(ErrorCodes.COMMENT_NOT_FOUND)
        comment.updateContent(
            requester = member,
            contents = contentSanitizer.sanitize(command.content)
        )
        commentRepository.save(comment)
    }
}