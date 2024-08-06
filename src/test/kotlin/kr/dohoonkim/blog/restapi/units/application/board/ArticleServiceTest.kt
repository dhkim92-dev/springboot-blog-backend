package kr.dohoonkim.blog.restapi.units.application.board

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleCreateCommand
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleDto
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleModifyCommand
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleSummaryDto
import kr.dohoonkim.blog.restapi.application.board.impl.ArticleServiceImpl
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes.*
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.domain.board.Article
import kr.dohoonkim.blog.restapi.domain.board.repository.ArticleRepository
import kr.dohoonkim.blog.restapi.domain.board.repository.CategoryRepository
import kr.dohoonkim.blog.restapi.support.entity.createArticles
import kr.dohoonkim.blog.restapi.support.entity.createCategory
import kr.dohoonkim.blog.restapi.support.entity.createMember
import org.springframework.data.repository.findByIdOrNull
import java.util.*

internal class ArticleServiceTest: BehaviorSpec({

    val articleRepository = mockk<ArticleRepository>()
    val categoryRepository = mockk<CategoryRepository>()
    val articleService = ArticleServiceImpl(articleRepository, categoryRepository)
    val member = createMember(1).first()
    val categories = createCategory(2)
    val articles = createArticles(member, categories[0], 10)
        .plus(createArticles(member, categories[1], 10))

    beforeSpec {

    }

    fun checkEqual(dto: ArticleDto, article: Article) {
        dto.id shouldBe article.id
        dto.title shouldBe article.title
        dto.contents shouldBe article.contents
        dto.category.id shouldBe article.category.id
        dto.viewCount shouldBe article.viewCount
        dto.author.id shouldBe article.author.id
    }

    fun checkEqual(dto: ArticleSummaryDto, article: Article) {
        dto.id shouldBe article.id
        dto.title shouldBe article.title
        dto.category.id shouldBe article.category.id
        dto.viewCount shouldBe article.viewCount
        dto.author.id shouldBe article.author.id
    }

    Given("게시물 생성 커맨드가 주어진다") {
        val command = ArticleCreateCommand(articles[0].title, articles[0].contents, articles[0].category.name)

        When("존재하지 않는 카테고리에 대해 게시물을 생성하면") {
            every { categoryRepository.findByName(any()) } returns null
            Then("NotFoundException이 발생한다") {
                shouldThrow<NotFoundException> {
                    articleService.createArticle(member.id, command)
                }.message shouldBe CATEGORY_NOT_FOUND.message
            }
        }

        When("존재하는 카테고리에 게시물을 생성하면") {
            every { categoryRepository.findByName(any()) } returns articles[0].category
            every { articleRepository.save(any()) } returns articles[0]
            val result = articleService.createArticle(member.id, command)
            Then("생성된다"){
                checkEqual(result, articles[0])
            }
        }
    }

    Given("아무것도 수정하지 않는 수정 커맨드가 주어진다") {
        val command = ArticleModifyCommand(
            title= null,
            contents = null,
            category = null
        )

        When("게시글을 수정하면") {
            every { articleRepository.findByIdOrNull(any()) } returns articles[0]
            every { articleRepository.save(any()) } returns articles[0]
            val originTitle = articles[0].title
            val originContents= articles[0].contents
            val originCategory = articles[0].category

            Then("수정되지 않는다") {
                val result = articleService.modifyArticle(member.id,articles[0].id, command)
                result.title shouldBe originTitle
                result.contents shouldBe originContents
                result.category.id shouldBe originCategory.id
            }
        }
    }

    Given("게시물 수정 커맨드가 주어진다") {
        val command = ArticleModifyCommand(
            title="new title",
            contents = null,
            category = categories[1].name
        )
        val target = articles[1]

        When("게시글을 찾을 수 없으면") {
            every { articleRepository.findByIdOrNull(any()) } returns null
            Then("NotFoundException이 발생한다"){
                shouldThrow<NotFoundException> {
                    articleService.modifyArticle(member.id, target.id, command)
                }.message shouldBe ARTICLE_NOT_FOUND.message
            }
        }

        When("요청 사용자와 작성자의 ID가 일치하지 않으면") {
            every { articleRepository.findByIdOrNull(any()) } returns target
            Then("ForbiddenException이 발생한다") {
                shouldThrow<ForbiddenException> {
                    articleService.modifyArticle(UUID.randomUUID(), target.id, command)
                }.message shouldBe RESOURCE_OWNERSHIP_VIOLATION.message
            }
        }

        When("존재하지 않는 카테고리로 변경하려 하면") {
            every { articleRepository.findByIdOrNull(any()) } returns target
            every { categoryRepository.findByName(any()) } returns null
            Then("NotFoundException이 발생한다") {
                shouldThrow<NotFoundException> {
                    articleService.modifyArticle(member.id, target.id, command)
                }.message shouldBe CATEGORY_NOT_FOUND.message
            }
        }

        When("필드가 모두 정상이면") {
            every { articleRepository.findByIdOrNull(any()) } returns target
            every { categoryRepository.findByName(any()) } returns categories[1]
            every { articleRepository.save(any()) } returns target

            Then("정상적으로 수정된다") {
                val dto = articleService.modifyArticle(member.id, target.id, command)
                checkEqual(dto, target)
                target.title shouldBe command.title
                target.category.id shouldBe categories[1].id
            }
        }
    }

    Given("삭제하려는 게시물 아이디가 주어진다") {
        val target = articles[2]

        When("게시글이 존재하지 않으면") {
            every {articleRepository.findByIdOrNull(any()) } returns null

            Then("NotFoundException이 발생한다") {
                shouldThrow<NotFoundException> {
                    articleService.deleteArticle(member.id, UUID.randomUUID())
                }.message shouldBe ARTICLE_NOT_FOUND.message
            }
        }

        When("삭제 요청 사용자와 게시글 사용자의 ID가 일치하지 않으면") {
            every { articleRepository.findByIdOrNull(any()) } returns target

            Then("Forbidden Exception이 발생한다") {
                shouldThrow<ForbiddenException> {
                    articleService.deleteArticle(UUID.randomUUID(), target.id)
                }.message shouldBe RESOURCE_OWNERSHIP_VIOLATION.message
            }
        }

        When("정상 요청이라면") {
            every { articleRepository.findByIdOrNull(any()) } returns target
            every { articleRepository.deleteById(any()) } returns Unit
            Then("삭제된다") {
                articleService.deleteArticle(member.id, target.id)
            }
        }
    }

    Given("조회하려는 게시물 ID가 주어진다") {
        When("존재하지 않는 게시물이면") {
            every { articleRepository.findByArticleId(any()) } throws NotFoundException(ARTICLE_NOT_FOUND)
            Then("NotFoundException이 발생한다") {
                shouldThrow<NotFoundException> {
                    articleService.getArticle(articles[0].id)
                }.message shouldBe ARTICLE_NOT_FOUND.message
            }
        }

        When("게시물이 존재하면") {
            every { articleRepository.findByArticleId(any()) } returns ArticleDto.fromEntity(articles[0])
            Then("ArticleDto 객체가 반환된다") {
                val result = articleService.getArticle(articles[0].id)
                checkEqual(result, articles[0])
            }
        }
    }

    Given("카테고리 ID이 주어진다") {
        val id = categories[0].id

        When("게시물 목록을 조회하면") {
            every { articleRepository.findArticles(id, null, any()) } returns articles.subList(0, 5)
                .map { it -> ArticleSummaryDto.fromEntity(it) }

            Then("해당 카테고리 게시글 ArticleSummaryDto 리스트가 반환된다") {
                val result = articleService.getListOfArticles(id, null,5 )
                result.forEachIndexed { index, value ->
                    checkEqual(value, articles[index])
                }
            }
        }
    }

    afterSpec {
        clearAllMocks()
    }
})