package kr.dohoonkim.blog.restapi.application.board.impl

import kr.dohoonkim.blog.restapi.application.board.ArticleService
import kr.dohoonkim.blog.restapi.application.board.dto.*
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.ARTICLES_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.ARTICLE_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.CATEGORIES_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.exceptions.EntityNotFoundException
import kr.dohoonkim.blog.restapi.common.utility.AuthenticationUtil
import kr.dohoonkim.blog.restapi.domain.article.Article
import kr.dohoonkim.blog.restapi.domain.article.repository.ArticleRepository
import kr.dohoonkim.blog.restapi.domain.article.repository.CategoryRepository
import kr.dohoonkim.blog.restapi.domain.member.Member
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional(readOnly = true)
class ArticleServiceImpl(
    private val articleRepository: ArticleRepository,
    private val categoryRepository: CategoryRepository,
    private val authenticationUtil: AuthenticationUtil
) : ArticleService {

    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * 신규 게시글을 생성한다.
     * 게시글은 관리자만 생성할 수 있기 때문에, Spring Security FilterChain에서 권한 체크를 해야한다.
     */
    @Transactional
    @Caching(
        evict = [
            CacheEvict(CATEGORIES_CACHE_KEY, allEntries = true),
            CacheEvict(ARTICLES_CACHE_KEY, allEntries = true)
        ],
        put = [CachePut(ARTICLE_CACHE_KEY, key = "#result.id")]
    )
    override fun createArticle(memberId: UUID, articleCreateDto: ArticleCreateDto): ArticleDto {
        val category = categoryRepository.findByName(articleCreateDto.category)
            ?: throw EntityNotFoundException(ErrorCode.CATEGORY_NOT_FOUND)
        val member = Member(id = memberId)
        val article = Article(
            author = member,
            title = articleCreateDto.title,
            contents = articleCreateDto.contents,
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
            CachePut(ARTICLE_CACHE_KEY, key = "#articleModifyDto.articleId.toString()")
        ]
    )
    override fun modifyArticle(memberId: UUID, articleModifyDto: ArticleModifyDto): ArticleDto {
        val article = articleRepository.findById(articleModifyDto.articleId)
            .orElseThrow { throw EntityNotFoundException(ErrorCode.ARTICLE_NOT_FOUND) }

        var category = categoryRepository.findByName(articleModifyDto.category)
            ?: throw EntityNotFoundException(ErrorCode.CATEGORY_NOT_FOUND)

        authenticationUtil.checkPermission(memberId, article.author.id)
        article.updateCategory(category)
        article.updateTitle(articleModifyDto.title)
        article.updateContents(articleModifyDto.contents)

        return ArticleDto.fromEntity(article)
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
        val article = articleRepository.findByArticleId(articleId)

        authenticationUtil.checkPermission(memberId, article.author.id);
        articleRepository.deleteById(article.id)
    }

    @Transactional
    @Cacheable(value = ["article"])
    override fun getArticle(articleId: UUID): ArticleDto {
        return articleRepository.findByArticleId(articleId)
    }

    @Transactional
    @Cacheable(value = ["articles"], unless = "#result.isEmpty()")
    override fun getListOfArticles(categoryId: Long, cursor: LocalDateTime?, pageSize: Long): List<ArticleSummaryDto> {
        return articleRepository.findArticles(categoryId, cursor, pageSize)
    }
}