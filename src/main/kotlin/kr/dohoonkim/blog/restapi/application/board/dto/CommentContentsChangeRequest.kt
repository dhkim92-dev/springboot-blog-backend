package kr.dohoonkim.blog.restapi.application.board.dto

import jakarta.validation.constraints.NotEmpty
import org.hibernate.validator.constraints.Length
import org.jetbrains.annotations.NotNull

data class CommentContentsChangeRequest(
    @field : Length(min = 1, max = 500)
    @field : NotEmpty
    val contents: String
)
