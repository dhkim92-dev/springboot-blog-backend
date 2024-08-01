package kr.dohoonkim.blog.restapi.interfaces.board.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryDto
import kr.dohoonkim.blog.restapi.application.board.dto.CategorySummaryDto

/**
 * 카테고리 요약 응답 객체
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 * @property id 카테고리 ID
 * @property name 카테고리 이름
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Schema(description = "카테고리 요약 응답 객체, 목록 조회 시 반환")
data class PostedCategorySummary(
    @Schema(description = "카테고리 ID", example = "1")
    val id: Long,
    @Schema(description = "카테고리 이름", example = "DevOps")
    val name: String
) {

    companion object {

        /**
         * @param dto CategorySummaryDto
         * @return PostedCategorySummary
         */
        fun valueOf(dto: CategorySummaryDto): PostedCategorySummary {
            return PostedCategorySummary(
                id = dto.id,
                name = dto.name
            )
        }

        /**
         * @param dto CategoryDto
         * @return PostedCategorySummary
         */
        fun valueOf(dto: CategoryDto): PostedCategorySummary {
            return PostedCategorySummary(
                id = dto.id,
                name = dto.name
            )
        }
    }
}
