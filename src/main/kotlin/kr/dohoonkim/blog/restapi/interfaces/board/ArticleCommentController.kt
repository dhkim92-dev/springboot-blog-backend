package kr.dohoonkim.blog.restapi.interfaces.board

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import kr.dohoonkim.blog.restapi.application.board.CommentServiceFacade
import kr.dohoonkim.blog.restapi.common.annotations.MemberId
import kr.dohoonkim.blog.restapi.common.response.ListResponse
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.common.response.annotation.ApplicationCode
import kr.dohoonkim.blog.restapi.interfaces.board.dto.CommentResponse
import kr.dohoonkim.blog.restapi.interfaces.board.dto.CreateCommentRequest
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
@RequestMapping("/api/v1")
class ArticleCommentController(
    private val commentService: CommentServiceFacade
) {

    @Operation(summary = "게시물 댓글 생성", description = "게시물 댓글 생성")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "게시물 댓글 생성 성공"),
    ])
    @PostMapping("/articles/{articleId}/comments")
    @ApplicationCode(ResultCode.CREATE_COMMENT_SUCCESS)
    @SecurityRequirement(name = "bearer-jwt")
    fun createComment(
        @MemberId loginId: UUID,
        @PathVariable articleId: UUID,
        @RequestBody @Valid request: CreateCommentRequest
    ) {
        commentService.create(loginId, request.toCommand(
            postId = articleId,
            parentId = null,
        ))
    }

    @Operation(summary = "게시물 대댓글 생성", description = "게시물 대댓글 생성")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = ""),
    ])
    @PostMapping("/articles/{articleId}/comments/{commentId}/replies")
    @ApplicationCode(ResultCode.CREATE_COMMENT_SUCCESS)
    @SecurityRequirement(name = "bearer-jwt")
    fun createReply(
        @MemberId loginId: UUID,
        @PathVariable articleId: UUID,
        @PathVariable commentId: UUID,
        @RequestBody @Valid request: CreateCommentRequest
    ) {
        commentService.create(loginId, request.toCommand(
            postId = articleId,
            parentId = commentId,
        ))
    }

    @Operation(summary = "댓글 및 대댓글 삭제", description = "댓글 및 대댓글 삭제")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "삭제 완료"),
    ])
    @DeleteMapping("/articles/{articleId}/comments/{commentId}")
    @ApplicationCode(ResultCode.DELETE_COMMENT_SUCCESS)
    @SecurityRequirement(name = "bearer-jwt")
    fun deleteComment(
        @MemberId loginId: UUID,
        @PathVariable commentId: UUID
    ) {
        commentService.delete(loginId, commentId)
    }

    @Operation(summary = "댓글 및 대댓글 수정", description = "댓글 및 대댓글을 수정합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "수정 성공"),
    ])
    @PutMapping("/articles/{articleId}/comments/{commentId}")
    @ApplicationCode(ResultCode.MODIFY_COMMENT_SUCCESS)
    @SecurityRequirement(name = "bearer-jwt")
    fun updateComment(
        @MemberId loginId: UUID,
        @PathVariable commentId: UUID,
        @RequestBody @Valid request: CreateCommentRequest
    ) {
        commentService.update(loginId, request.toCommand(commentId))
    }

    @Operation(summary = "게시물 댓글 조회", description = "게시물의 댓글을 조회합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "댓글 조회 성공"),
    ])
    @GetMapping("/articles/{articleId}/comments")
    @ApplicationCode(ResultCode.GET_COMMENT_LIST_SUCCESS)
    fun getComments(
        @PathVariable articleId: UUID,
        @RequestParam cursor: UUID?,
        @RequestParam(defaultValue = "40") size: Int = 40
    ): ListResponse<CommentResponse> {
        val comments = commentService.getPostComments(
            postId = articleId,
            cursor = cursor,
            size = size
        )

        return ListResponse.of(
            size = size,
            items = comments.map { CommentResponse.from(it) },
            extractors = mapOf(
               "cursor" to { it.id },
               "size" to { size }
            )
        )
    }

    @Operation(summary = "댓글의 대댓글 조회", description = "특정 댓글의 대댓글을 조회합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "대댓글 조회 성공"),
    ])
    @GetMapping("/articles/{articleId}/comments/{commentId}/replies")
    @ApplicationCode(ResultCode.GET_RELY_COMMENT_LIST_SUCCESS)
    fun getReplies(
        @PathVariable commentId: UUID,
        @RequestParam cursor: UUID?,
        @RequestParam(defaultValue = "40") size: Int = 40
    ): ListResponse<CommentResponse> {
        val replies = commentService.getCommentReplies(
            commentId = commentId,
            cursor = cursor,
            size = size
        )

        return ListResponse.of(
            size = size,
            items = replies.map { CommentResponse.from(it) },
            extractors = mapOf(
                "cursor" to { it.id },
                "size" to { size }
            )
        )
    }

}