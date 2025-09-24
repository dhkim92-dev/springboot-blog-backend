package kr.dohoonkim.blog.restapi.application.board.impl.article

import kr.dohoonkim.blog.restapi.application.board.dto.article.UpdatePostCommand
import kr.dohoonkim.blog.restapi.application.board.usecases.article.UpdatePostUseCase
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.ARTICLES_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.ARTICLE_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.CATEGORIES_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.port.persistence.board.ArticleRepository
import kr.dohoonkim.blog.restapi.port.persistence.board.CategoryRepository
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Caching
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class UpdatePostUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val categoryRepository: CategoryRepository,
    private val articleRepository: ArticleRepository
): UpdatePostUseCase {

    override fun update(loginId: UUID, command: UpdatePostCommand) {
        val member = memberRepository.findByIdOrNull(loginId)
            ?: throw ForbiddenException(ErrorCodes.NO_PERMISSION)
        val article = articleRepository.findByIdOrNull(command.postId)
            ?: throw NotFoundException(ErrorCodes.ARTICLE_NOT_FOUND)
        val newCategory = command.categoryId?.let {
            categoryRepository.findByIdOrNull(it)
        }
        article.updateCategory(member, newCategory)
        article.updateTitle(member, command.title)
        article.updateContents(member, command.content)
        articleRepository.save(article)
    }
}