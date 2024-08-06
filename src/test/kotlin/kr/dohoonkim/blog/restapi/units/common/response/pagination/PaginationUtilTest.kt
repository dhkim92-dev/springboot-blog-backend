package kr.dohoonkim.blog.restapi.units.common.response.pagination

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.application.member.dto.MemberDto
import kr.dohoonkim.blog.restapi.common.response.CursorList
import kr.dohoonkim.blog.restapi.common.response.pagination.Cursor
import kr.dohoonkim.blog.restapi.common.response.pagination.PaginationUtil
import kr.dohoonkim.blog.restapi.common.utility.UrlUtility
import kr.dohoonkim.blog.restapi.domain.board.Article
import kr.dohoonkim.blog.restapi.interfaces.board.ArticleController
import kr.dohoonkim.blog.restapi.interfaces.board.CategoryController
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostedArticleSummary
import kr.dohoonkim.blog.restapi.support.createPostedArticleDtoList
import kr.dohoonkim.blog.restapi.support.mockCursorMethodParameter
import kr.dohoonkim.blog.restapi.support.mockMethodParameter
import org.springframework.core.MethodParameter
import java.lang.reflect.Method
import java.time.LocalDateTime
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.javaMethod

internal class PaginationUtilTest: AnnotationSpec() {

    private val urlUtility = mockk<UrlUtility>()

    private val paginationUtil = PaginationUtil(urlUtility)

    private lateinit var articles: List<PostedArticleSummary>

    @BeforeEach
    fun setUp() {
        every { urlUtility.getURL() } returns "https://www.dohoon-kim.kr/api/v1/members"
        val controller = ArticleController::class
        articles = createPostedArticleDtoList(40)
    }

    @Test
    fun `size 값을 입력한 경우 데이터 크기가 size보다 크면 size가 next에 붙어야한다`() {
        val queries = mapOf("size" to "10")
        val parameters = listOf(
            mockCursorMethodParameter("categoryId", "category.id", true),
            mockCursorMethodParameter("createdAt", "${LocalDateTime.now().toString()}", false),
        )
        val cursorList = paginationUtil.toCursorList(articles, parameters, queries) as CursorList<PostedArticleSummary>

        cursorList.next shouldNotBe null
        cursorList.next!!.contains("size=10") shouldBe true
        cursorList.count shouldBe 10
        cursorList.data.forEachIndexed{
                index, it -> it.id shouldBe articles[index].id
        }
    }

    @Test
    fun `size 값을 입력한 경우 데이터 크기가 size보다 작으면 next는 null`() {
        val queries = mapOf("size" to "50")
        val parameters = listOf(
            mockCursorMethodParameter("categoryId", "category.id", true),
            mockCursorMethodParameter("createdAt", "${LocalDateTime.now().toString()}", false),
        )
        val cursorList = paginationUtil.toCursorList(articles, parameters, queries) as CursorList<PostedArticleSummary>

        cursorList.next shouldBe null
        cursorList.count shouldBe articles.size
        cursorList.data.forEachIndexed{
                index, it -> it.id shouldBe articles[index].id
        }
    }

    @Test
    fun `inherit이 true인 경우 값이 그대로 유지되어야 한다`() {
        val queries = mapOf("createdAt" to LocalDateTime.now().toString(), "categoryId" to "9999")
        val parameters = listOf(
            mockCursorMethodParameter("categoryId", "category.id", true),
            mockCursorMethodParameter("createdAt", "${LocalDateTime.now().toString()}", false),
        )
        val cursorList = paginationUtil.toCursorList(articles, parameters, queries) as CursorList<PostedArticleSummary>

        cursorList.next shouldNotBe null
        println(cursorList.next)
        cursorList.next!!.contains("categoryId=9999") shouldBe true
        cursorList.count shouldBe 20
        cursorList.data.forEachIndexed{
                index, it -> it.id shouldBe articles[index].id
        }
    }

    @Test
    fun `createdAt이 쿼리에 포함 되어있고 data가 기본 반환(20) 보다 크면 next가 null이 아니다`() {
        val queries = mapOf("createdAt" to LocalDateTime.now().toString())
        val parameters = listOf(
            mockCursorMethodParameter("categoryId", "", false),
            mockCursorMethodParameter("createdAt", "${LocalDateTime.now().toString()}", false),
        )
        val cursorList = paginationUtil.toCursorList(articles, parameters, queries) as CursorList<PostedArticleSummary>

        cursorList.next shouldNotBe null
        cursorList.count shouldBe 20
        cursorList.data.forEachIndexed{
            index, it -> it.id shouldBe articles[index].id
        }
    }

    @Test
    fun `createdAt이 쿼리에 포함 되어있고 data가 기본 반환(20) 보다 작으면 next가 null이다`() {
        val queries = mapOf("createdAt" to LocalDateTime.now().toString())
        val parameters = listOf(
            mockCursorMethodParameter("categoryId", "", false),
            mockCursorMethodParameter("createdAt", "${LocalDateTime.now().toString()}", false),
        )
        val cursorList = paginationUtil.toCursorList(articles.subList(0, 10), parameters, queries) as CursorList<PostedArticleSummary>

        cursorList.next shouldBe null
        cursorList.count shouldBe 10
        cursorList.data.forEachIndexed{
                index, it -> it.id shouldBe articles[index].id
        }
    }

    @AfterEach
    fun clearMocks() {
        clearAllMocks()
    }
}