package kr.dohoonkim.blog.restapi.application.member.dto

import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class NicknameChangeCommand(
    val memberId: UUID,
    @field: NotBlank(message = "nickname can not be null")
    val nickname: String
)
