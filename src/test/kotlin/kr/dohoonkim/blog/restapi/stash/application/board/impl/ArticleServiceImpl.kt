package kr.dohoonkim.blog.restapi.stash.application.board.impl

import kr.dohoonkim.blog.restapi.application.board.ArticleService
import kr.dohoonkim.blog.restapi.application.board.dto.*
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.ARTICLES_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.ARTICLE_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.CATEGORIES_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.stash.domain.entity.Article
import kr.dohoonkim.blog.restapi.stash.domain.repository.ArticleRepository
import kr.dohoonkim.blog.restapi.stash.domain.repository.CategoryRepository
import kr.dohoonkim.blog.restapi.domain.member.Member
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

/**
 * ArticleService 구현체
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 * @see ArticleService
 */
@Service
@Transactional(readOnly = true)
class ArticleServiceImpl(
    private val articleRepository: ArticleRepository,
    private val categoryRepository: CategoryRepository,
) : ArticleService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    @Caching(
        evict = [
            CacheEvict(CATEGORIES_CACHE_KEY, allEntries = true),
            CacheEvict(ARTICLES_CACHE_KEY, allEntries = true)
        ],
        put = [CachePut(ARTICLE_CACHE_KEY, key = "#result.id")]
    )
    override fun createArticle(memberId: UUID, request: ArticleCreateCommand): ArticleDto {
        val category = categoryRepository.findByName(request.category)
            ?: throw NotFoundException(ErrorCodes.CATEGORY_NOT_FOUND)
        val article = Article(
            author = Member(memberId),
            title = request.title,
            contents = request.contents,
            category = category
        )

        return ArticleDto.fromEntity(articleRepository.save(article))
    }

    @Transactional
    @Caching(
        evict = [
            CacheEvict(CATEGORIES_CACHE_KEY, allEntries = true),
            CacheEvict(ARTICLES_CACHE_KEY, allEntries = true),
        ],
        put = [
            CachePut(ARTICLE_CACHE_KEY, key = "#articleId.toString()")
        ]
    )
    override fun modifyArticle(memberId: UUID, articleId: UUID, request: ArticleModifyCommand): ArticleDto {
        val article = articleRepository.findByIdOrNull(articleId)
            ?: throw NotFoundException(ErrorCodes.ARTICLE_NOT_FOUND)

        if(memberId != article.author.id) {
            throw ForbiddenException(ErrorCodes.RESOURCE_OWNERSHIP_VIOLATION)
        }

        val category = if(request.category!=null) {
            categoryRepository.findByName(request.category)
                ?: throw NotFoundException(ErrorCodes.CATEGORY_NOT_FOUND)
        } else {
            null
        }

        article.updateCategory(category)
        article.updateTitle(request.title)
        article.updateContents(request.contents)

        return ArticleDto.fromEntity(articleRepository.save(article))
    }

    @Transactional
    @Caching(
        evict = [
            CacheEvict(CATEGORIES_CACHE_KEY, allEntries = true),
            CacheEvict(ARTICLES_CACHE_KEY, allEntries = true),
            CacheEvict(ARTICLE_CACHE_KEY, key = "#articleId")
        ]
    )
    override fun deleteArticle(memberId: UUID, articleId: UUID): Unit {
        val article = articleRepository.findByIdOrNull(articleId)
            ?: throw NotFoundException(ErrorCodes.ARTICLE_NOT_FOUND)

        if(memberId != article.author.id) {
            throw ForbiddenException(ErrorCodes.RESOURCE_OWNERSHIP_VIOLATION)
        }

        articleRepository.deleteById(article.id)
    }

    @Transactional
    @Cacheable(value = ["article"])
    override fun getArticle(articleId: UUID): ArticleDto {
        return articleRepository.findByArticleId(articleId)
    }

    @Transactional
    @Cacheable(value = ["articles"], unless = "#result.isEmpty()")
    override fun getListOfArticles(categoryId: Long, cursor: LocalDateTime?, pageSize: Int): List<ArticleSummaryDto> {
        return articleRepository.findArticles(categoryId, cursor, pageSize+1)
    }
}