package kr.dohoonkim.blog.restapi.application.board.usecases.category

import kr.dohoonkim.blog.restapi.application.board.dto.category.UpdateCategoryCommand
import java.util.UUID

interface UpdateCategoryUseCase {

    fun update(loginId: UUID, command: UpdateCategoryCommand)
}