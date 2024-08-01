package kr.dohoonkim.blog.restapi.interfaces.board.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import kr.dohoonkim.blog.restapi.application.board.dto.ArticleSummaryDto
import kr.dohoonkim.blog.restapi.interfaces.member.vo.MemberSummaryVo
import java.time.LocalDateTime
import java.util.UUID


/**
 * 게시물 요약 객체
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 * @property id 게시물 ID
 * @property title 게시물 제목
 * @property category 게시물 카테고리 요약
 * @property author 게시물 작성자 정보 요약
 * @property createdAt 게시물 생성일
 * @property viewCount 조회 수
 * @property contains 댓글 수
 */
@Schema(description = "게시물 요약 객체, 리스트 반환에서 사용")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PostedArticleSummary(
    @Schema(description = "게시물 ID", example = "efd12e72-8be4-4308-80d5-870de9300117")
    val id: UUID,
    @Schema(description = "게시물 제목", example = "[프로그래머스] Lv4 - 안티 세포")
    val title: String,
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
         * Service Layer에서 넘어온 ArticleSummaryDto 를 PostedArticleSummary로 변환한다.
         * @param dto 게시물 요약 객체
         * @return 게시물 요약 응답 객체
         */
        fun valueOf(dto: ArticleSummaryDto): PostedArticleSummary {
            return PostedArticleSummary(
                id = dto.id,
                title = dto.title,
                category = PostedCategorySummary.valueOf(dto.category),
                author = MemberSummaryVo.valueOf(dto.author),
                createdAt = dto.createdAt,
                viewCount = dto.viewCount,
                commentCount = dto.commentCount
            )
        }
    }
}
