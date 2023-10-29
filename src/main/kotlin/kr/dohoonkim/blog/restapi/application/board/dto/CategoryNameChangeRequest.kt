package kr.dohoonkim.blog.restapi.application.board.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CategoryNameChangeRequest(
    @field : Length(min = 1, max = 16)
    @field : NotBlank
    val newName: String
)
