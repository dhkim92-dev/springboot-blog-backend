package kr.dohoonkim.blog.restapi.interfaces.board.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.dohoonkim.blog.restapi.application.board.dto.category.CategoryDto
import kr.dohoonkim.blog.restapi.common.response.BaseResponse

@Schema(description = "카테고리 응답")
data class CategoryResponse(
    @Schema(description = "카테고리 ID", example = "1")
    val id: Long,
    @Schema(description = "카테고리 이름", example = "공지사항")
    val name: String,
    @Schema(description = "카테고리 게시물 수", example = "10")
    val count: Long
): BaseResponse() {

    companion object {
        fun from(dto: CategoryDto): CategoryResponse {
            return CategoryResponse(
                id = dto.id,
                name = dto.name,
                count = dto.count
            )
        }
    }
}