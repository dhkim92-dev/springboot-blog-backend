package kr.dohoonkim.blog.restapi.application.board.impl.article

import kr.dohoonkim.blog.restapi.application.board.dto.article.CreatePostCommand
import kr.dohoonkim.blog.restapi.application.board.usecases.article.CreatePostUseCase
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.domain.board.Article
import kr.dohoonkim.blog.restapi.port.persistence.board.ArticleRepository
import kr.dohoonkim.blog.restapi.port.persistence.board.CategoryRepository
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional()
class CreatePostUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val articleRepository: ArticleRepository,
    private val categoryRepository: CategoryRepository
) : CreatePostUseCase {

    override fun create(loginId: UUID, command: CreatePostCommand): UUID {
        val member = memberRepository.findByIdOrNull(loginId)
            ?: throw ForbiddenException(ErrorCodes.NO_PERMISSION)
        val category = categoryRepository.findByIdOrNull(command.categoryId)
            ?: throw NotFoundException(ErrorCodes.CATEGORY_NOT_FOUND)
        val article = Article.create(
            member = member,
            category = category,
            title = command.title,
            contents = command.content
        )
        return articleRepository.save(article).id!!
    }
}