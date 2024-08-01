package kr.dohoonkim.blog.restapi.units.interfaces

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.application.board.CategoryService
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryCreateCommand
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryDto
import kr.dohoonkim.blog.restapi.interfaces.board.dto.ModifyCategoryRequest
import kr.dohoonkim.blog.restapi.interfaces.board.CategoryController
import kr.dohoonkim.blog.restapi.support.entity.createCategory

class CategoryControllerTest : AnnotationSpec() {
    private val categoryService = mockk<CategoryService>()
    private val categoryController = CategoryController(categoryService)
    private val category = createCategory()
    private val url = "/api/v1/article-categories"
    private val data = listOf(
        CategoryDto.fromEntity(createCategory()),
        CategoryDto.fromEntity(createCategory()),
        CategoryDto.fromEntity(createCategory())
    )

    @Test
    fun `카테고리를 추가한다`() {
        val request = CategoryCreateCommand(name = category.name)
        val data = CategoryDto.fromEntity(category)

        every { categoryService.createCategory(any()) } returns data

        val response = categoryController.createCategory(request)

        response shouldBe data
    }

    @Test
    fun `카테고리 목록을 받아온다`() {
        every { categoryService.getCategories() } returns data

        val response = categoryController.getCategories()

        response.count shouldBe data.size
        for(i in 0 until data.size) {
            response.data[i] shouldBe data[i]
        }
    }

    @Test
    fun `카테고리 이름을 수정한다`() {
        category.changeName("new-name")
        val data = CategoryDto.fromEntity(category)
        val request = ModifyCategoryRequest(name = "new-name")

        every { categoryService.modifyCategoryName(any()) } returns data

        val response = categoryController.updateName(category.id, request)
        response shouldBe data
    }

    @Test
    fun `카테고리를 삭제한다`() {
        every { categoryService.deleteCategory(category.id) } returns Unit

        shouldNotThrowAny {
            val response = categoryController.delete(category.id)
        }
    }

    @AfterEach
    fun `clearMocks`() {
        clearAllMocks()
    }

}