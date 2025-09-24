package kr.dohoonkim.blog.restapi.application.board.usecases.category

import java.util.UUID

interface DeleteCategoryUseCase {

    fun delete(loginId: UUID, categoryId: Long)
}