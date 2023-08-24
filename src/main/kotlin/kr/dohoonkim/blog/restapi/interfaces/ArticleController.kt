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
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.application.board.dto.*
import kr.dohoonkim.blog.restapi.common.response.CursorList
import kr.dohoonkim.blog.restapi.common.utility.CursorListBuilder
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("api/")
@Tag(name = "Article API", description = "게시물 API")
@ApiResponses(value = [
    ApiResponse(responseCode = "401", description = "G002 - 사용자 인증 실패",
            content = [Content(schema = Schema(implementation=ErrorResponse::class))]),
    ApiResponse(responseCode = "403", description = "G003 - 이용 권한 없음",
            content = [Content(schema = Schema(implementation=ErrorResponse::class))])
])
class ArticleController(private val articleService: ArticleService) {

    private val DEFAULT_PAGINATION_SIZE = 20L
    private val log = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "게시물 등록", description = "게시물을 등록한다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "P001 - 게시물 생성 성공"),
    ])
    @PostMapping("v1/articles")
    fun createArticle(@RequestBody @Valid request : ArticleCreateRequest): ResponseEntity<ApiResult<ArticleDto>> {
        val dto = ArticleCreateDto(
            title = request.title,
            contents = request.contents,
            category = request.category
        )

        return Ok(ResultCode.CREATE_ARTICLE_SUCCESS, articleService.createArticle(dto))
    }

    @Operation(summary = "게시물 조회", description = "게시물의 내용을 조회한다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "P004 - 게시물 조회 성공"),
        ApiResponse(responseCode = "404", description = "P001 - 존재하지 않는 게시물에 대한 요청",
            content = [Content(schema = Schema(implementation=ErrorResponse::class))])
    ])
    @GetMapping("v1/articles/{articleId}")
    fun getArticle(@PathVariable articleId : UUID) : ResponseEntity<ApiResult<ArticleDto>> {
        log.info("v1/articles/${articleId} called.")
        return Ok(ResultCode.GET_ARTICLE_SUCCESS, articleService.getArticle(articleId))
    }


    @Operation(summary = "게시물 목록 조회", description = "게시물 목록을 조회한다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "P005 - 게시물 목록 조회 성공"),
    ])
    @GetMapping("v1/articles")
    fun getArticles(@RequestParam(required = false) category : String?,
                    @RequestParam(required = false) createdAt : LocalDateTime?,
                    @RequestParam(required = false) direction : String?)
    : ResponseEntity<ApiResult<CursorList<ArticleSummaryDto>>> {
        val articles = articleService.getListOfArticles(category, DEFAULT_PAGINATION_SIZE, createdAt, direction)
        log.info("Articles size : ${articles.size}")
        val data = CursorListBuilder.build(articles, listOf("createdAt"), DEFAULT_PAGINATION_SIZE)

        return Ok(ResultCode.GET_ARTICLE_LIST_SUCCESS, data)
    }

    @Operation(summary = "게시물 수정", description = "게시물의 제목, 본문, 카테고리등을 수정한다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "P002 - 게시물 수정 성공"),
        ApiResponse(responseCode = "404", description = "P001 - 존재하지 않는 게시물에 대한 요청",
            content = [Content(schema = Schema(implementation=ErrorResponse::class))])
    ])
    @PatchMapping("v1/articles/{articleId}")
    fun updateArticle(@RequestBody @Valid request : ArticleModifyRequest, @PathVariable articleId : UUID)
    : ResponseEntity<ApiResult<ArticleDto>> {
        val dto = ArticleModifyDto(articleId = articleId, title = request.title, contents = request.contents, category = request.category)

        return Ok(ResultCode.MODIFY_ARTICLE_SUCCESS, articleService.modifyArticle(dto))
    }

    @Operation(summary = "게시물 삭제", description = "P003 - 게시물을 삭제합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "P003 - 게시물을 삭제하였습니다."),
        ApiResponse(responseCode = "404", description = "P001 - 존재하지 않는 게시물에 대한 요청",
            content = [Content(schema = Schema(implementation=ErrorResponse::class))])
    ])
    @DeleteMapping("v1/articles/{articleId}")
    fun deleteArticle(@PathVariable articleId : UUID) = Ok(ResultCode.DELETE_ARTICLE_SUCCESS, articleService.deleteArticle(articleId))

}