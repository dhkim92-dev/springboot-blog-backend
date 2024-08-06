package kr.dohoonkim.blog.restapi.interfaces.board

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.dohoonkim.blog.restapi.application.board.ArticleService
import kr.dohoonkim.blog.restapi.common.error.ErrorResponse
import kr.dohoonkim.blog.restapi.application.board.dto.*
import kr.dohoonkim.blog.restapi.security.annotations.MemberId
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.common.response.ResultCode.*
import kr.dohoonkim.blog.restapi.common.response.annotation.ApplicationCode
import kr.dohoonkim.blog.restapi.common.response.pagination.Cursor
import kr.dohoonkim.blog.restapi.common.response.pagination.CursorPagination
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostArticleRequest
import kr.dohoonkim.blog.restapi.interfaces.board.dto.ModifyArticleRequest
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostedArticle
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostedArticleSummary
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

/**
 * 게시물 API 컨트롤러
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 */
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
    private val logger = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "게시물 등록", description = "게시물을 등록한다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "P001 - 게시물 생성 성공"),
        ]
    )
    @PostMapping("v1/admin/articles")
    @ResponseStatus(HttpStatus.CREATED)
    @ApplicationCode(ResultCode.CREATE_ARTICLE_SUCCESS)
    fun createArticle(
        @RequestBody @Valid request: PostArticleRequest,
        @MemberId memberId: UUID
    ): PostedArticle {
        val dto = ArticleCreateCommand(
            title = request.title,
            contents = request.contents,
            category = request.category
        )

        return PostedArticle.valueOf(articleService.createArticle(memberId, dto))
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
    @ApplicationCode(GET_ARTICLE_SUCCESS)
    fun getArticle(@PathVariable articleId: UUID): PostedArticle {
        return PostedArticle.valueOf(articleService.getArticle(articleId))
    }

    @Operation(summary = "카테고리별 게시물 목록 조회", description = "카테고리의 게시물 목록을 조회한다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "P005 - 게시물 목록 조회 성공"),
        ]
    )
    @GetMapping("v1/articles")
    @ApplicationCode(GET_ARTICLE_LIST_SUCCESS)
    @CursorPagination
    fun getArticles(
        @RequestParam(required = false) @Cursor(inherit = true) categoryId: Long?,
        @RequestParam(required = false) @Cursor createdAt: LocalDateTime?,
        @RequestParam(required = false, defaultValue = "20") size: Int
    ) : List<PostedArticleSummary> {
        return articleService.getListOfArticles(categoryId ?: 0, createdAt, size)
            .map{ it -> PostedArticleSummary.valueOf(it) }
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
    @PatchMapping("v1/admin/articles/{articleId}")
    @ApplicationCode(MODIFY_ARTICLE_SUCCESS)
    fun updateArticle(
        @RequestBody @Valid request: ModifyArticleRequest,
        @PathVariable articleId: UUID,
        @MemberId memberId: UUID
    ): PostedArticle {
        val command = ArticleModifyCommand(
            title = request.title,
            contents = request.contents,
            category = request.category
        )

        return PostedArticle.valueOf(articleService.modifyArticle(memberId, articleId, command))
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
    @DeleteMapping("v1/admin/articles/{articleId}")
    @ApplicationCode(DELETE_ARTICLE_SUCCESS)
    fun deleteArticle(
        @PathVariable articleId: UUID,
        @MemberId memberId: UUID
    ) {
        logger.debug("member Id : $memberId")
        articleService.deleteArticle(memberId, articleId)
    }
}