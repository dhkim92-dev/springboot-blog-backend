package kr.dohoonkim.blog.restapi.interfaces

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.dohoonkim.blog.restapi.application.board.ArticleService
import kr.dohoonkim.blog.restapi.common.error.ErrorResponse
import kr.dohoonkim.blog.restapi.common.response.ApiResult
import kr.dohoonkim.blog.restapi.common.response.ApiResult.Companion.Ok
import kr.dohoonkim.blog.restapi.application.board.dto.*
import kr.dohoonkim.blog.restapi.security.annotations.MemberId
import kr.dohoonkim.blog.restapi.common.response.CursorList
import kr.dohoonkim.blog.restapi.common.response.ResultCode.*
import kr.dohoonkim.blog.restapi.common.utility.CursorListBuilder
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("api/")
@Tag(name = "Article API", description = "게시물 API")
@ApiResponses(
    value = [
        ApiResponse(
            responseCode = "401", description = "G002 - 사용자 인증 실패",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        ),
        ApiResponse(
            responseCode = "403", description = "G003 - 이용 권한 없음",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )
    ]
)
class ArticleController(private val articleService: ArticleService) {

    private val DEFAULT_PAGINATION_SIZE = 20L
    private val log = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "게시물 등록", description = "게시물을 등록한다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "P001 - 게시물 생성 성공"),
        ]
    )
    @PostMapping("v1/articles")
    fun createArticle(
        @RequestBody @Valid request: ArticleCreateRequest,
        @MemberId memberId: UUID
    )
            : ResponseEntity<ApiResult<ArticleDto>> {
        val dto = ArticleCreateDto(
            title = request.title,
            contents = request.contents,
            category = request.category
        )

        return Ok(CREATE_ARTICLE_SUCCESS, articleService.createArticle(memberId, dto))
    }

    @Operation(summary = "게시물 조회", description = "게시물의 내용을 조회한다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "P004 - 게시물 조회 성공"),
            ApiResponse(
                responseCode = "404", description = "P001 - 존재하지 않는 게시물에 대한 요청",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @GetMapping("v1/articles/{articleId}")
    fun getArticle(@PathVariable articleId: UUID)
            : ResponseEntity<ApiResult<ArticleDto>> {
        return Ok(GET_ARTICLE_SUCCESS, articleService.getArticle(articleId))
    }

    @Operation(summary = "카테고리별 게시물 목록 조회", description = "카테고리의 게시물 목록을 조회한다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "P005 - 게시물 목록 조회 성공"),
        ]
    )
    @GetMapping("v1/articles")
    fun getArticles(
        @RequestParam(required = false) categoryId: Long?,
        @RequestParam(required = false) createdAt: LocalDateTime?
    )
            : ResponseEntity<ApiResult<CursorList<ArticleSummaryDto>>> {
        val articles = articleService.getListOfArticles(categoryId ?: 0, createdAt, DEFAULT_PAGINATION_SIZE)
        val queryMap = mutableMapOf<String, String>("created_at" to "createdAt")
        if (categoryId != null && categoryId != 0L) queryMap.put("category#id", "categoryId")

        val data = CursorListBuilder.build(articles, queryMap, DEFAULT_PAGINATION_SIZE)

        return Ok(GET_ARTICLE_LIST_SUCCESS, data)
    }

    @Operation(summary = "게시물 수정", description = "게시물의 제목, 본문, 카테고리등을 수정한다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "P002 - 게시물 수정 성공"),
            ApiResponse(
                responseCode = "404", description = "P001 - 존재하지 않는 게시물에 대한 요청",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PatchMapping("v1/articles/{articleId}")
    fun updateArticle(
        @RequestBody @Valid request: ArticleModifyRequest,
        @PathVariable articleId: UUID,
        @MemberId memberId: UUID
    )
            : ResponseEntity<ApiResult<ArticleDto>> {
        val dto = ArticleModifyDto(
            articleId = articleId,
            title = request.title,
            contents = request.contents,
            category = request.category
        )

        return Ok(MODIFY_ARTICLE_SUCCESS, articleService.modifyArticle(memberId, dto))
    }

    @Operation(summary = "게시물 삭제", description = "P003 - 게시물을 삭제합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "P003 - 게시물을 삭제하였습니다."),
            ApiResponse(
                responseCode = "404", description = "P001 - 존재하지 않는 게시물에 대한 요청",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @DeleteMapping("v1/articles/{articleId}")
    fun deleteArticle(
        @PathVariable articleId: UUID,
        @MemberId memberId: UUID
    ) = Ok(DELETE_ARTICLE_SUCCESS, articleService.deleteArticle(memberId, articleId))

}