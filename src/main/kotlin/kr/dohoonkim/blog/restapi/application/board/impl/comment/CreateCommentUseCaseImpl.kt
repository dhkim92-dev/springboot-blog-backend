package kr.dohoonkim.blog.restapi.application.board.impl.comment

import kr.dohoonkim.blog.restapi.application.board.dto.comment.CommentDto
import kr.dohoonkim.blog.restapi.application.board.dto.comment.CreateCommentCommand
import kr.dohoonkim.blog.restapi.application.board.usecases.comment.CreateCommentUseCase
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.common.utility.ContentSanitizer
import kr.dohoonkim.blog.restapi.domain.board.Comment
import kr.dohoonkim.blog.restapi.port.persistence.board.ArticleRepository
import kr.dohoonkim.blog.restapi.port.persistence.board.CommentRepository
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class CreateCommentUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
    private val contentSanitizer: ContentSanitizer
)
: CreateCommentUseCase {

    override fun create(loginId: UUID, command: CreateCommentCommand)
    : CommentDto {
        val member = memberRepository.findByIdOrNull(loginId)
            ?: throw NotFoundException(ErrorCodes.MEMBER_NOT_FOUND)
        val article = articleRepository.getReferenceById(command.postId)
        val parentComment = command.parentId?.let {
            commentRepository.findByIdOrNull(it)
                ?: throw NotFoundException(ErrorCodes.COMMENT_NOT_FOUND)
        }

        val comment = Comment.create(
            writer = member,
            article = article,
            parent = parentComment,
            content = contentSanitizer.sanitize(command.content)
        )

        return CommentDto.from(commentRepository.save(comment))
    }
}