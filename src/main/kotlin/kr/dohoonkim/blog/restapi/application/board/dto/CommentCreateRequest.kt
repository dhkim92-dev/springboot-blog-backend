package kr.dohoonkim.blog.restapi.application.board.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.hibernate.validator.constraints.Length
import org.jetbrains.annotations.NotNull

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CommentCreateRequest(
    val parentId: Long? = null,
    @field : NotNull
    @field : Length(min = 1, max = 500, message = "댓글은 1글자 이상 500자 이하입니다.")
    val contents: String
)
