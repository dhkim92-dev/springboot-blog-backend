package kr.dohoonkim.blog.restapi.interfaces.board.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

/**
 * 카테고리 이름 수정 요청 객체
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 * @property name 수정될 카테고리 이름
 */
@Schema(description = "카테고리 수정 요청 객체")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ModifyCategoryRequest(
    @field : NotBlank(message = "카테고리 이름이 입력되어야 합니다")
    @field : Length(min = 1, max = 16, message = "카테고리 이름은 1자 이상 16자 이하입니다")
    @Schema(description = "변경될 카테고리 이름", required = true, example = "Devops")
    val name: String
)
