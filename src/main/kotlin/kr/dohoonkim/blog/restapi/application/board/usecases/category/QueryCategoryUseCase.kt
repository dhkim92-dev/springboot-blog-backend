package kr.dohoonkim.blog.restapi.application.board.usecases.category

import kr.dohoonkim.blog.restapi.application.board.dto.category.CategoryDto

interface QueryCategoryUseCase {

    fun getCategories(): List<CategoryDto>
}