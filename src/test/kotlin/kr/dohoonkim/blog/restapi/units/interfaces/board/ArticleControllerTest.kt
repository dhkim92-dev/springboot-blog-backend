package kr.dohoonkim.blog.restapi.units.interfaces.board

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.application.board.ArticleService
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleDto
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleSummaryDto
import kr.dohoonkim.blog.restapi.domain.board.Article
import kr.dohoonkim.blog.restapi.domain.board.Category
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.interfaces.board.ArticleController
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostArticleRequest
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostedArticle
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostedArticleSummary
import kr.dohoonkim.blog.restapi.support.entity.createArticle
import kr.dohoonkim.blog.restapi.support.entity.createArticles
import kr.dohoonkim.blog.restapi.support.entity.createCategory
import kr.dohoonkim.blog.restapi.support.entity.createMember
import kr.dohoonkim.blog.restapi.support.web.modifyArticleRequest
import kr.dohoonkim.blog.restapi.support.web.postArticleRequest

internal class ArticleControllerTest: BehaviorSpec({

    val articleService = mockk<ArticleService>(relaxed = true)
    val articleController = ArticleController(articleService)
    lateinit var member: Member
    lateinit var articles: List<Article>
    lateinit var categories: List<Category>

    fun checkEqual(postedArticle: PostedArticle, article: Article) {
        postedArticle.id shouldBe article.id
        postedArticle.viewCount shouldBe article.viewCount
        postedArticle.title shouldBe article.title
        postedArticle.contents shouldBe article.contents
        postedArticle.category.id shouldBe article.category.id
        postedArticle.category.name shouldBe article.category.name
        postedArticle.createdAt shouldBe article.createdAt
        postedArticle.author.id shouldBe article.author.id
        postedArticle.author.nickname shouldBe article.author.nickname
    }

    fun checkEqual(postedArticleSummary: PostedArticleSummary, article: Article) {
        postedArticleSummary.id shouldBe article.id
        postedArticleSummary.viewCount shouldBe article.viewCount
        postedArticleSummary.title shouldBe article.title
        postedArticleSummary.category.id shouldBe article.category.id
        postedArticleSummary.category.name shouldBe article.category.name
        postedArticleSummary.createdAt shouldBe article.createdAt
        postedArticleSummary.author.id shouldBe article.author.id
        postedArticleSummary.author.nickname shouldBe article.author.nickname
    }

    beforeSpec {
        member = createMember(1).first()
        categories = createCategory(2)
        articles = createArticles(member, categories[0], 10).plus(
            createArticles(member, categories[1], 10)
        )
    }

    Given("게시글 생성 요청이 주어진다") {
        val request = postArticleRequest(articles[0])

        When("생성되면") {
            every { articleService.createArticle(any(), any()) } returns ArticleDto.fromEntity(articles[0])
            val result = articleController.createArticle(request, member.id)

            Then("PostedArticle이 반환된다") {
                (result is PostedArticle) shouldBe true
                checkEqual(result, articles[0])
            }
        }
    }

        Given("게시글 수정 요청이 주어진다") {
        val request = modifyArticleRequest(articles[0])

        When("수정되면") {
            every {articleService.modifyArticle(any(), any(), any())} returns ArticleDto.fromEntity(articles[0])
            val result = articleController.updateArticle(request, articles[0].id, member.id)
            Then("PostedArticle이 반환된다") {
                (result is PostedArticle) shouldBe true
                checkEqual(result, articles[0])
            }
        }
    }

    Given("게시글 삭제 요청이 주어진다") {
        When("요청이 처리되면") {
            every { articleService.deleteArticle(any(), any()) } returns Unit
            Then("Unit이 반환된다") {
                val result=articleController.deleteArticle(
                    articles[0].id,
                    articles[0].author.id
                )
                result shouldBe Unit
            }
        }
    }

    Given("게시글 단건 조회 요청이 주어진다") {
        val articleId = articles[0].id
        When("게시글이 존재하면") {
            every { articleService.getArticle(articleId) } returns ArticleDto.fromEntity(articles[0])
            Then("PostedArticle이 반환된다") {
                val result = articleController.getArticle(articleId)
                (result is PostedArticle) shouldBe true
                checkEqual(result, articles[0])
            }
        }
    }

    Given("게시글 목록 조회 요청이 주어진다") {
        val size = 10
        When("조회되면") {
            every { articleService.getListOfArticles(any(), any(), any()) } returns articles.subList(0, size+1)
                .map { it -> ArticleSummaryDto.fromEntity(it) }
            Then("PostedArticleSummary List가 반환된다") {
                val result = articleController.getArticles(null, null, size)
                (result is List<*>) shouldBe true
                (result[0] is PostedArticleSummary) shouldBe true
                result.forEachIndexed { index, postedArticleSummary -> checkEqual(postedArticleSummary, articles[index]) }
            }
        }
    }

    afterSpec {
        clearAllMocks()
    }
})