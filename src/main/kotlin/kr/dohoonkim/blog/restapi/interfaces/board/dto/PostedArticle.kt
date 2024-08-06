package kr.dohoonkim.blog.restapi.interfaces.board.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleDto
import kr.dohoonkim.blog.restapi.interfaces.member.dto.MemberSummaryVo
import java.time.LocalDateTime
import java.util.UUID


/**
 * 게시물 전체 정보 응답 객체
 * @property id 게시물 ID
 * @property title 게시물 제목
 * @property contents 게시물 본문
 * @property author 게시물 작성자 정보 요약
 * @property createdAt 게시일
 * @property viewCount 조회 수
 * @property commentCount 댓글 수
 */
@Schema(description = "게시물 정보 상세")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PostedArticle(
    @Schema(description = "게시물 ID", example = "efd12e72-8be4-4308-80d5-870de9300117")
    val id: UUID,
    @Schema(description = "게시물 제목", example = "[프로그래머스] Lv4 - 안티 세포")
    val title: String,
    @Schema(description = "게시물 본문", example = "text field")
    val contents: String,
    @Schema(description = "카테고리 정보")
    val category: PostedCategorySummary,
    @Schema(description = "작성자 정보")
    val author: MemberSummaryVo,
    @Schema(description = "게시일", example = "2024-07-11T10:00:00.000Z")
    val createdAt: LocalDateTime,
    @Schema(description = "조회 수", example = "1")
    val viewCount: Long,
    @Schema(description = "댓글 수", example = "1")
    val commentCount: Long
) {

    companion object {

        /**
         * Service Layer 에서 반환된 DTO 를 응답 객체로 변환한다
         * @param dto ArticleDto 객체
         */
        fun valueOf(dto: ArticleDto): PostedArticle {
            return PostedArticle(
                id = dto.id,
                title = dto.title,
                contents = dto.contents,
                category = PostedCategorySummary.valueOf(dto.category),
                author = MemberSummaryVo.valueOf(dto.author),
                createdAt = dto.createdAt,
                viewCount = dto.viewCount,
                commentCount = dto.commentCount
            )
        }
    }
}