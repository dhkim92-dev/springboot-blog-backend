package kr.dohoonkim.blog.restapi.application.board.usecases.category

import kr.dohoonkim.blog.restapi.application.board.dto.category.CategoryDto
import kr.dohoonkim.blog.restapi.application.board.dto.category.CreateCategoryCommand
import java.util.UUID

interface CreateCategoryUseCase {

    fun create(loginId: UUID, command: CreateCategoryCommand): CategoryDto
}