package kr.dohoonkim.blog.restapi.application.board.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.hibernate.validator.constraints.Length

data class ArticleModifyRequest(
    @field: NotEmpty(message = "제목이 주어져야합니다.")
    @field: Length(min = 1, max = 255, message = "제목은 최소 1글자 최대 255글자 입니다.")
    val title: String,
    @field: NotEmpty(message = "본문 내용이 주어져야합니다.")
    val contents: String,
    @field: NotBlank(message = "카테고리 명은 공백문자가 올 수 없습니다.")
    @field: Length(min = 1, max = 16, message = "카테고리 명은 최소 1글자 최대 16글자입니다.")
    val category: String
) {
}