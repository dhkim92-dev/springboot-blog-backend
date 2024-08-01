package kr.dohoonkim.blog.restapi.units.interfaces

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kr.dohoonkim.blog.restapi.application.board.ArticleService
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostArticleRequest
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleDto
import kr.dohoonkim.blog.restapi.interfaces.board.dto.ModifyArticleRequest
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleSummaryDto
import kr.dohoonkim.blog.restapi.common.response.ApiResult
import kr.dohoonkim.blog.restapi.common.response.ResultCode.*
import kr.dohoonkim.blog.restapi.common.utility.CursorListBuilder
import kr.dohoonkim.blog.restapi.domain.member.Role
import kr.dohoonkim.blog.restapi.interfaces.board.ArticleController
import kr.dohoonkim.blog.restapi.support.entity.createArticle
import kr.dohoonkim.blog.restapi.support.entity.createCategory
import kr.dohoonkim.blog.restapi.support.entity.createDummyArticles
import kr.dohoonkim.blog.restapi.support.entity.createMember

class ArticleControllerTest : AnnotationSpec() {
    private val articleService : ArticleService = mockk()
    private val admin = createMember(role = Role.ADMIN)
    private val category = createCategory()
    private val category2 = createCategory()
    private val article = createArticle(admin, category)
    private val url = "/api/v1/articles"
    private val dummy = listOf(createDummyArticles(admin, category, delay = 10L),
        createDummyArticles(admin, category2, delay = 10L)).flatten()
    private val articleController = ArticleController(articleService)
    private val objectMapper = ObjectMapper().registerModule(kotlinModule())

    @AfterEach
    fun `Mockk 삭제`() {
        unmockkObject(CursorListBuilder)
        clearAllMocks()
    }

    @Test
    fun `게시물을 생성한다`() {
        val request = PostArticleRequest(title = article.title, contents = article.contents, category = category.name)
        val data = ArticleDto.fromEntity(article)
        val response = ApiResult.Ok(CREATE_ARTICLE_SUCCESS, data)

        every { articleService.createArticle(admin.id, any()) } returns data
        val result = articleController.createArticle(request, admin.id)

        result shouldBe data
    }

    @Test
    fun `게시물을 조회한다`() {
        val data = ArticleDto.fromEntity(article)
        val response = ApiResult.Ok(GET_ARTICLE_SUCCESS, data)

        every { articleService.getArticle(any()) } returns data

        val result = articleController.getArticle(data.id)
        result shouldBe data
    }

    @Test
    fun `전체 게시물 목록을 조회한다`() {
        var data = dummy.map { it -> ArticleSummaryDto.fromEntity(it) }.subList(0, 11)
        val cursorData = CursorListBuilder.build(data, mapOf("createdAt" to "createdAt"), 20, false)

        mockkObject(CursorListBuilder)
        every { CursorListBuilder.build(data, any(), any(), any()) } returns cursorData
        every { articleService.getListOfArticles(any(), any(), any()) } returns data
        val result = articleController.getArticles(null, null)

        result.count shouldBe 11
        for(i in 0 until result.count) {
            result.data[i] shouldBe data[i]
        }
    }

    @Test
    fun `특정 카테고리의 게시물을 조회한다`() {
        val data = dummy.filter { it.category == category }
            .map{ it -> ArticleSummaryDto.fromEntity(it) }
        val cursorData = CursorListBuilder.build(data, mapOf("createdAt" to "createdAt"), 20L, false)
        val response = ApiResult.Ok(GET_ARTICLE_LIST_SUCCESS, cursorData)

        mockkObject(CursorListBuilder)
        every { CursorListBuilder.build(data, any(), any(), any()) } returns cursorData
        every { articleService.getListOfArticles(any(), any(), any()) } returns data
        val result = articleController.getArticles( category.id,null)

        result.count shouldBe cursorData.count

        for(i in 0 until result.count) {
            result.data[i] shouldBe data[i]
        }
    }

    @Test
    fun `커서와 함께 조회한다`() {
        val data = dummy.subList(10, 19)
            .map{ it -> ArticleSummaryDto.fromEntity(it) }
        val cursorData = CursorListBuilder.build(data, mapOf("createdAt" to "createdAt"),20L, false)
        val response = ApiResult.Ok(GET_ARTICLE_LIST_SUCCESS, cursorData)

        mockkObject(CursorListBuilder)
        every { CursorListBuilder.build(data, any(), any(), any()) } returns cursorData
        every { articleService.getListOfArticles(any(), any(), any()) } returns data
        val result = articleController.getArticles(null, dummy[9].createdAt)

        result.count shouldBe data.size
        for(i in 0 until result.count) {
            result.data[i] shouldBe data[i]
        }
    }

    @Test
    fun `게시물을 수정한다`() {
        article.updateContents("수정된 본문")
        val request = ModifyArticleRequest(article.title, article.contents, article.category.name)
        val data = ArticleDto.fromEntity(article)

        every { articleService.modifyArticle(article.author.id, any()) } returns data
        val result = articleController.updateArticle(request, article.id, article.author.id)

        result shouldBe data
    }

    @Test
    fun `게시물을 삭제한다`() {
        every { articleService.deleteArticle(article.author.id, article.id) } returns Unit
        val result = articleController.deleteArticle(article.id, article.author.id)
        result shouldBe Unit
    }

}