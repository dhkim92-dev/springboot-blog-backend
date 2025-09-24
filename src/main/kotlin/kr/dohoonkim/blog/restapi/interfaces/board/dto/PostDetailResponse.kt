package kr.dohoonkim.blog.restapi.interfaces.board.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.dohoonkim.blog.restapi.application.board.dto.article.PostDto
import kr.dohoonkim.blog.restapi.application.board.dto.article.PostSummaryDto
import kr.dohoonkim.blog.restapi.application.member.dto.MemberSummaryDto
import kr.dohoonkim.blog.restapi.common.response.BaseResponse
import java.time.LocalDateTime
import java.util.Locale
import java.util.UUID

@Schema(description = "게시글 상세 응답")
data class PostDetailResponse(
    @Schema(description = "게시글 ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    val id: UUID,
    @Schema(description = "게시글 제목", example = "게시글 제목입니다.")
    val title: String,
    @Schema(description = "게시글 내용", example = "게시글 내용입니다.")
    val content: String,
    val writer: WriterDto,
    val category: CategoryResponse,
    @Schema(description = "게시글 작성일시", example = "2021-01-01T00:00:00")
    val createdAt: LocalDateTime,
    @Schema(description = "조회 수", example = "100")
    val viewCount: Long = 0L,
    @Schema(description = "댓글 수", example = "10")
    val commentCount: Long = 0L,
    @Schema(description = "좋아요 수", example = "50")
    val likeCount: Long = 0L,
): BaseResponse() {

    companion object {
        fun from(dto: PostDto): PostDetailResponse {
            return PostDetailResponse(
                id = dto.id,
                title = dto.title,
                content = dto.content,
                writer = WriterDto.from(dto.author),
                category = CategoryResponse.from(dto.category),
                createdAt = dto.createdAt
            )
        }

        fun from(dto: PostSummaryDto): PostDetailResponse {
            return PostDetailResponse(
                id = dto.id,
                title = dto.title,
                content = "",
                writer = WriterDto.from(dto.author),
                category = CategoryResponse.from(dto.category),
                createdAt = dto.createdAt
            )
        }
    }
}