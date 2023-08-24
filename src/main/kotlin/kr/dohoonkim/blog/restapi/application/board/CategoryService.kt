package kr.dohoonkim.blog.restapi.application.board

import kr.dohoonkim.blog.restapi.application.board.dto.CategoryCreateDto
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryDto
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryModifyDto
import kr.dohoonkim.blog.restapi.common.response.CursorList

interface CategoryService {

    fun createCategory(dto : CategoryCreateDto) : CategoryDto
    fun modifyCategoryName(dto : CategoryModifyDto) : CategoryDto
    fun getCategories() : List<CategoryDto>
    fun deleteCategory(categoryId : Long) : Unit

}