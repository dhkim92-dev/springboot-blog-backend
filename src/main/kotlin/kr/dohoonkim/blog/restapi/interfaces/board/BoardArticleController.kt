package kr.dohoonkim.blog.restapi.interfaces.board

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.dohoonkim.blog.restapi.application.board.PostServiceFacade
import kr.dohoonkim.blog.restapi.common.annotations.MemberId
import kr.dohoonkim.blog.restapi.common.response.ListResponse
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.common.response.annotation.ApplicationCode
import kr.dohoonkim.blog.restapi.interfaces.board.dto.CreatePostRequest
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostDetailResponse
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@Tag(name = "Board Article", description = "게시글 관련 API")
@RequestMapping("/api/v1/articles")
class BoardArticleController(
    private val postService: PostServiceFacade
) {

    @Operation(summary = "게시물 생성", description = "게시물 생성")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "게시물 생성 성공"),
    ])
    @PostMapping("")
    @ApplicationCode(ResultCode.CREATE_ARTICLE_SUCCESS)
    @SecurityRequirement(name = "bearer-jwt")
    fun createArticle(
        @MemberId loginId: UUID,
        @RequestBody @Valid request: CreatePostRequest
    ): PostResponse {
        val postId = postService.createPost(loginId, request.toCommand())
        return PostResponse(id = postId)
    }

    @PutMapping("/{postId}")
    @Operation(summary = "게시물 수정", description = "게시물 수정")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "게시물 수정 성공"),
    ])
    @ApplicationCode(ResultCode.MODIFY_ARTICLE_SUCCESS)
    @SecurityRequirement(name = "bearer-jwt")
    fun updateArticle(
        @MemberId loginId: UUID,
        @PathVariable postId: UUID,
        @RequestBody @Valid request: CreatePostRequest
    ) {
        postService.updatePost(loginId, request.toCommand(postId))
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시물 삭제", description = "게시물 삭제")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "게시물 삭제 성공"),
    ])
    @ApplicationCode(ResultCode.DELETE_ARTICLE_SUCCESS)
    @SecurityRequirement(name = "bearer-jwt")
    fun deleteArticle(
        @MemberId loginId: UUID,
        @PathVariable postId: UUID
    ) {
        postService.deletePost(loginId, postId)
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시물 조회", description = "게시물 조회")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "게시물 조회 성공"),
    ])
    @ApplicationCode(ResultCode.GET_ARTICLE_SUCCESS)
    fun getArticle(
        @PathVariable postId: UUID
    ): PostDetailResponse {
        val post = postService.getPost(postId)
        return PostDetailResponse.from(post)
    }

    @GetMapping("")
    @Operation(summary = "게시물 목록 조회", description = "게시물 목록 조회")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "게시물 목록 조회 성공"),
    ])
    @ApplicationCode(ResultCode.GET_ARTICLE_LIST_SUCCESS)
    fun getArticles(
        @RequestParam(required = false) categoryId: Long?,
        @RequestParam(required = false) cursor: UUID?,
        @RequestParam(required = false, defaultValue = "20") size: Int
    ): ListResponse<PostDetailResponse> {
        val posts = postService.getPosts(categoryId, cursor, size+1)
            .asSequence()
            .map { PostDetailResponse.from(it) }
            .toList()

        return ListResponse.of(
            size = size,
            items = posts,
            extractors = buildMap {
                categoryId?.let { put("categoryId") { categoryId } }
                put("cursor") { it.id }
                put("size") { size }
            }
        )
    }
}