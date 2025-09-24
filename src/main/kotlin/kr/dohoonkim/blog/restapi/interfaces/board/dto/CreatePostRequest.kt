package kr.dohoonkim.blog.restapi.interfaces.board.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import kr.dohoonkim.blog.restapi.application.board.dto.article.CreatePostCommand
import kr.dohoonkim.blog.restapi.application.board.dto.article.UpdatePostCommand
import org.hibernate.sql.Update
import java.util.UUID

@Schema(description = "게시글 등록 요청")
class CreatePostRequest(
    @Schema(description = "카테고리 ID", example = "1")
    val categoryId: Long,
    @Schema(description = "게시글 제목", example = "게시글 제목입니다.")
    @field: NotEmpty(message = "제목은 비어 있을 수 없습니다.")
    val title: String,
    @Schema(description = "게시글 내용", example = "게시글 내용입니다.")
    @field: NotEmpty(message = "내용은 비어 있을 수 없습니다.")
    val content: String
) {

    fun toCommand(): CreatePostCommand {
        return CreatePostCommand(
            categoryId = categoryId,
            title = title,
            content = content
        )
    }

    fun toCommand(postId: UUID): UpdatePostCommand {
        return UpdatePostCommand(
            postId = postId,
            categoryId = categoryId,
            title = title,
            content = content
        )
    }
}