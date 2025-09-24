package kr.dohoonkim.blog.restapi.application.board.impl.article

import kr.dohoonkim.blog.restapi.application.board.dto.article.PostDto
import kr.dohoonkim.blog.restapi.application.board.dto.article.PostSummaryDto
import kr.dohoonkim.blog.restapi.application.board.usecases.article.QueryPostUseCase
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.port.persistence.board.ArticleRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class QueryPostUseCaseImpl(private val articleRepository: ArticleRepository)
: QueryPostUseCase {

    override fun getPost(postId: UUID): PostDto {
        val post = articleRepository.getArticleById(postId)
            ?: throw NotFoundException(ErrorCodes.ARTICLE_NOT_FOUND)

        return PostDto.from(post)
    }

    override fun getPosts(
        categoryId: Long?,
        cursor: UUID?,
        size: Int
    ): List<PostSummaryDto> {
        return articleRepository.getArticlesByCategoryIdWithPagination(
            categoryId = categoryId,
            cursor = cursor,
            size = size)
            .asSequence()
            .map(PostSummaryDto::from)
            .toList()
    }
}