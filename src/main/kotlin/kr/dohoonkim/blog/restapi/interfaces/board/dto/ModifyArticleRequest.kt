package kr.dohoonkim.blog.restapi.interfaces.board.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.annotation.Nullable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.hibernate.validator.constraints.Length

/**
 * 게시물 수정 요청 객체
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 * @property title 게시물 제목
 * @property contents 게시물 본문
 * @property category 카테고리 이름
 */
@Schema(description = "게시물 수정 요청 본문")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ModifyArticleRequest(
    @field: Length(min = 1, max = 255, message = "제목은 최소 1글자 최대 255글자 입니다.")
    @Schema(description = "게시물 제목", example = "스프링부트 백엔드", required = false)
    val title: String?,

    @Schema(description = "게시물 본문", example = "스프링부트로 홈페이지 백엔드 교체", required = false)
    val contents: String?,

    @Schema(description = "카테고리 이름", example = "Web", required = false)
    @field: Length(min = 1, max = 16, message = "카테고리 명은 최소 1글자 최대 16글자입니다.")
    val category: String?
) {
}