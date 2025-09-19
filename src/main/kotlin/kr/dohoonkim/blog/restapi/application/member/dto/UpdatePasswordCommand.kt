package kr.dohoonkim.blog.restapi.application.member.dto

import java.util.UUID

class UpdatePasswordCommand(
    val targetId: UUID,
    val currentPassword: String,
    val newPassword: String
) {
}