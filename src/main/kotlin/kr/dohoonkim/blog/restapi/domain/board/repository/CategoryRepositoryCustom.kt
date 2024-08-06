package kr.dohoonkim.blog.restapi.domain.board.repository

import kr.dohoonkim.blog.restapi.application.board.dto.CategoryDto

interface CategoryRepositoryCustom {

    fun existsByName(name: String): Boolean

    fun existsByCategoryId(id: Long): Boolean

    fun findAllCategory(): List<CategoryDto>

}