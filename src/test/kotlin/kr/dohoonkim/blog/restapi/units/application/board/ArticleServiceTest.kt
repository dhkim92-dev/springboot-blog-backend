package kr.dohoonkim.blog.restapi.units.application.board

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.application.board.ArticleService
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleCreateDto
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleDto
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleModifyDto
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleSummaryDto
import kr.dohoonkim.blog.restapi.application.board.impl.ArticleServiceImpl
import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.ErrorCode.ARTICLE_NOT_FOUND
import kr.dohoonkim.blog.restapi.common.error.ErrorCode.RESOURCE_OWNERSHIP_VIOLATION
import kr.dohoonkim.blog.restapi.common.error.exceptions.EntityNotFoundException
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.utility.AuthenticationUtil
import kr.dohoonkim.blog.restapi.domain.article.Category
import kr.dohoonkim.blog.restapi.domain.article.repository.ArticleRepository
import kr.dohoonkim.blog.restapi.domain.article.repository.CategoryRepository
import kr.dohoonkim.blog.restapi.domain.member.Role
import kr.dohoonkim.blog.restapi.support.entity.createArticle
import kr.dohoonkim.blog.restapi.support.entity.createCategory
import kr.dohoonkim.blog.restapi.support.entity.createMember
import java.util.*

class ArticleServiceTest : BehaviorSpec({

    val articleRepository = mockk<ArticleRepository>()
    val categoryRepository = mockk<CategoryRepository>()
    val authenticationUtils = mockk<AuthenticationUtil>()
    val articleService: ArticleService = ArticleServiceImpl(articleRepository, categoryRepository, authenticationUtils)
    val admin = createMember(role = Role.ADMIN)
    val category = createCategory()
    val article = createArticle(admin, category)
    val articleCreateDto =
        ArticleCreateDto(title = article.title, contents = article.contents, category = category.name)
    val articleModifyDto = ArticleModifyDto(
        articleId = article.id,
        title = "modified-title",
        contents = "modified-contents",
        category = "modified-category-name"
    )

    Given("게시물 목록을 조회한다.") {
        val category2 = createCategory()
        var articleList: MutableList<ArticleSummaryDto> = mutableListOf()

        for (i in 0..20) {
            articleList.add(ArticleSummaryDto.fromEntity(createArticle(admin, category)))
            articleList.add(ArticleSummaryDto.fromEntity(createArticle(admin, category2)))
            Thread.sleep(50)
        }


        When("카테고리 목록이 주어졌을 때") {
            every { articleRepository.findArticles(category.id, any(), any()) } returns articleList.filter {
                it.category.name == category.name
            }

            Then("해당 카테고리의 게시글 목록만 반환된다.") {
                val ret = articleService.getListOfArticles(category.id, null, 20)

                ret.size shouldBe 21
                ret.forEach {
                    it.category.name shouldBe category.name
                }
            }
        }

        When("카테고리 목록이 주어지지 않을 때") {
            every { articleRepository.findArticles(any(), any(), any()) } returns articleList.subList(0, 20)

            Then("전체 카테고리에 대해 조회된다.") {
                val ret = articleService.getListOfArticles(0L, null, 20)

                ret.size shouldBe 20
                ret[0].category.name shouldBe category.name
                ret[1].category.name shouldBe category2.name
            }
        }

        When("커서가 주어지면") {
            articleList.sortBy { it.createdAt }
            articleList.reverse()
            val cursor = articleList[4].createdAt

            every { articleRepository.findArticles(any(), any(), any()) } returns articleList.filter {
                it.createdAt <= cursor
            }

            Then("커서 이전의 값들이 반환된다.") {
                val ret = articleService.getListOfArticles(0L, cursor, 20)

                ret.forEach {
                    it.createdAt shouldBeLessThanOrEqualTo cursor
                }
            }
        }

    }

    Given("게시물을 조회한다.") {
        When("게시물이 존재하면") {
            every { articleRepository.findByArticleId(any()) } returns ArticleDto.fromEntity(article)

            Then("게시글이 조회된다.") {
                val ret = articleService.getArticle(article.id)

                ret.id shouldBe article.id
                ret.author.id shouldBe article.author.id
                ret.category.name shouldBe article.category.name
                ret.title shouldBe article.title
                ret.contents shouldBe article.contents
            }
        }

        When("게시물이 존재하지 않으면") {
            every { articleRepository.findByArticleId(any()) } throws EntityNotFoundException(ARTICLE_NOT_FOUND)

            Then("에러가 발생한다.") {
                shouldThrow<EntityNotFoundException> {
                    articleService.getArticle(article.id)
                }
            }
        }
    }

    Given("사용자 인증 정보가 있다.") {
//        every{ authenticationUtils.extractMemberId() } returns admin.id
//        every{ authenticationUtils.extractAuthenticationMember() } returns admin
        every { authenticationUtils.checkPermission(admin.id, admin.id) } returns Unit

        When("존재하는 카테고리에 게시글을 작성하면") {
            every { articleRepository.save(any()) } returns article
            every { categoryRepository.findByName(any()) } returns category

            Then("게시글 작성이 성공한다.") {
                val newArticle = articleService.createArticle(admin.id, articleCreateDto)

                newArticle.id shouldBe article.id
                newArticle.title shouldBe article.title
                newArticle.contents shouldBe article.contents
                newArticle.author.id shouldBe admin.id
                newArticle.category.name shouldBe category.name
            }
        }

        When("존재하지 않는 카테고리에 게시글을 작성하면") {
            every { articleRepository.save(any()) } returns article
            every { categoryRepository.findByName(any()) } throws EntityNotFoundException(ErrorCode.CATEGORY_NOT_FOUND)
            val dto = ArticleCreateDto(title = article.title, contents = article.contents, category = category.name)

            Then("에러가 발생한다.") {
                shouldThrow<EntityNotFoundException> {
                    articleService.createArticle(admin.id, dto)
                }
            }
        }

        When("정상 수정 정보로 자신의 글을 수정하면") {
            val newCategory = Category(name = articleModifyDto.category)
            every { articleRepository.save(any()) } returns article
            every { categoryRepository.findByName(any()) } returns newCategory
            every { categoryRepository.existsByName(any()) } returns true
            every { articleRepository.findById(any()) } returns Optional.of(article)

            Then("게시글이 변경된다.") {
                val ret = articleService.modifyArticle(admin.id, articleModifyDto)

                ret.id shouldBe article.id
                ret.title shouldBe articleModifyDto.title
                ret.contents shouldBe articleModifyDto.contents
                ret.category.name shouldBe articleModifyDto.category
            }
        }

        When("다른 사람의 글을 수정하면") {
            every { articleRepository.save(any()) } returns article
            every { categoryRepository.findByName(any()) } returns category
            every { categoryRepository.existsByName(any()) } returns true
            every { articleRepository.findById(any()) } returns Optional.of(createArticle(createMember(), category))
            every { authenticationUtils.checkPermission(any(), any()) } throws ForbiddenException(
                RESOURCE_OWNERSHIP_VIOLATION
            )

            Then("에러가 발생한다.") {
                shouldThrow<ForbiddenException> {
                    articleService.modifyArticle(admin.id, articleModifyDto)
                }
            }
        }

        When("자신의 글을 삭제하면") {
            every { articleRepository.findByArticleId(any()) } returns ArticleDto.fromEntity(article)
            every { articleRepository.deleteById(any()) } returns Unit
            every { authenticationUtils.checkPermission(any(), any()) } returns Unit

            Then("삭제 된다.") {
                articleService.deleteArticle(article.author.id, article.id) shouldBe Unit
            }
        }

        When("다른 사람의 글을 삭제하면") {
            val othersArticle = createArticle(createMember(), category)
            every { articleRepository.findByArticleId(any()) } returns ArticleDto.fromEntity(othersArticle)
            every { articleRepository.deleteById(any()) } returns Unit
            every { authenticationUtils.checkPermission(any(), any()) } throws ForbiddenException(
                RESOURCE_OWNERSHIP_VIOLATION
            )

            Then("에러가 발생한다.") {
                shouldThrow<ForbiddenException> {
                    articleService.deleteArticle(admin.id, othersArticle.id)
                }
            }
        }
    }

    afterProject {
        clearAllMocks()
    }

})