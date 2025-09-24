package kr.dohoonkim.blog.restapi.interfaces.board.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import kr.dohoonkim.blog.restapi.application.board.dto.category.CreateCategoryCommand
import kr.dohoonkim.blog.restapi.application.board.dto.category.UpdateCategoryCommand

@Schema(description = "카테고리 생성 요청")
data class CreateCategoryRequest(
    @field: NotEmpty(message = "카테고리 이름은 필수입니다.")
    @Schema(description = "카테고리 이름", example = "공지사항", required = true)
    val name: String
) {
    fun toCommand(): CreateCategoryCommand {
        return CreateCategoryCommand(
            name = this.name
        )
    }

    fun toCommand(categoryId: Long): UpdateCategoryCommand {
        return UpdateCategoryCommand(
            id = categoryId,
            name = this.name
        )
    }
}