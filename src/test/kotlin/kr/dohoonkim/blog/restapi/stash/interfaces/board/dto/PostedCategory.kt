package kr.dohoonkim.blog.restapi.stash.interfaces.board.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryDto

/**
 * 카테고리 요약 응답 객체
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 * @property id 카테고리 ID
 * @property name 카테고리 이름
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Schema(description = "카테고리 응답 객체, 목록 조회 시 반환")
data class PostedCategory(
    @Schema(description = "카테고리 ID", example = "1")
    val id: Long,
    @Schema(description = "카테고리 이름", example = "DevOps")
    val name: String,
    @Schema(description = "카테고리 게시물 개수", example = "32")
    val count: Long
) {

    companion object {

        /**
         * @param dto CategoryDto
         * @return PostedCategory
         */
        fun valueOf(dto: CategoryDto): PostedCategory {
            return PostedCategory(id=dto.id, name=dto.name, count=dto.count)
        }
    }
}