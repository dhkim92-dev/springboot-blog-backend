package kr.dohoonkim.blog.restapi.interfaces.board.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.dohoonkim.blog.restapi.common.response.BaseResponse
import java.util.UUID

@Schema(description = "게시글 등록 응답")
class PostResponse(
    @Schema(description = "게시글 ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    val id: UUID
): BaseResponse() {
}