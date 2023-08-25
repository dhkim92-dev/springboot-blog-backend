package kr.dohoonkim.blog.restapi.application.board.impl

import kr.dohoonkim.blog.restapi.application.board.ArticleService
import kr.dohoonkim.blog.restapi.application.board.dto.*
import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.exceptions.EntityNotFoundException
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.utility.AuthenticationUtil
import kr.dohoonkim.blog.restapi.domain.article.Article
import kr.dohoonkim.blog.restapi.domain.article.repository.ArticleRepository
import kr.dohoonkim.blog.restapi.domain.article.repository.CategoryRepository
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnauthorizedException
import kr.dohoonkim.blog.restapi.domain.member.Member
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional(readOnly = true)
class ArticleServiceImpl(
    private val articleRepository: ArticleRepository,
    private val categoryRepository: CategoryRepository,
    private val authenticationUtil: AuthenticationUtil)
    : ArticleService {

    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * 신규 게시글을 생성한다.
     * 게시글은 관리자만 생성할 수 있기 때문에, Spring Security FilterChain에서 권한 체크를 해야한다.
     */
    @Transactional
    override fun createArticle(articleCreateDto: ArticleCreateDto) : ArticleDto {
        val memberId = authenticationUtil.extractMemberId()
            ?: throw UnauthorizedException(ErrorCode.AUTHENTICATION_FAIL)
        val category = categoryRepository.findByName(articleCreateDto.category)
            ?: throw EntityNotFoundException(ErrorCode.CATEGORY_NOT_FOUND)
        val member = Member(id = memberId)
        val article = Article(author = member, title = articleCreateDto.title, contents = articleCreateDto.contents, category = category)

        return ArticleDto.fromEntity(articleRepository.save(article))
    }

    @Transactional
    override fun modifyArticle(articleModifyDto: ArticleModifyDto) : ArticleDto {
        val memberId = authenticationUtil.extractMemberId()
                ?: throw UnauthorizedException(ErrorCode.AUTHENTICATION_FAIL)
        val article = articleRepository.findById(articleModifyDto.articleId)
            .orElseThrow { throw EntityNotFoundException(ErrorCode.ARTICLE_NOT_FOUND) }

        if(memberId != article.author.id) {
            throw ForbiddenException(ErrorCode.RESOURCE_OWNERSHIP_VIOLATION)
        }

        var category = categoryRepository.findByName(articleModifyDto.category)
            ?: throw EntityNotFoundException(ErrorCode.CATEGORY_NOT_FOUND)

        article.updateCategory(category)
        article.updateTitle(articleModifyDto.title)
        article.updateContents(articleModifyDto.contents)

        return ArticleDto.fromEntity(article)
    }

    @Transactional
    override fun deleteArticle(articleId : UUID) : Unit {
        val article = articleRepository.findByArticleId(articleId)
            ?: throw EntityNotFoundException(ErrorCode.ARTICLE_NOT_FOUND)

        if(!authenticationUtil.isAdmin() && !authenticationUtil.isResourceOwner(article.author.id)) {
            throw ForbiddenException(ErrorCode.RESOURCE_OWNERSHIP_VIOLATION)
        }

        articleRepository.deleteById(article.id)
    }

    @Transactional
    override fun getArticle(articleId: UUID): ArticleDto {
        return articleRepository.findByArticleId(articleId)
            ?: throw EntityNotFoundException(ErrorCode.ARTICLE_NOT_FOUND)
    }

    @Transactional
    override fun getListOfArticles(categoryId: Long, cursor: LocalDateTime?, direction: String?, pageSize: Long): List<ArticleSummaryDto> {
        return articleRepository.findArticles(categoryId, cursor, direction, pageSize)
    }

}