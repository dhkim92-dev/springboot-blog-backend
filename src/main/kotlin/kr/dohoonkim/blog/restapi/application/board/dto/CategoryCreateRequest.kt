package kr.dohoonkim.blog.restapi.application.board.dto

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class CategoryCreateRequest(
    @field: NotBlank(message = "카테고리명에는 공백문자가 올 수 없습니다.")
    @field: Length(min = 1, max = 16, message = "카테고리 명은 최소 1글자 최대 16글자입니다.")
    val name: String
)
