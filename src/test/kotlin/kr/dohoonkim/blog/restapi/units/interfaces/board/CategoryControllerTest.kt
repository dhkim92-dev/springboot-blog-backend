package kr.dohoonkim.blog.restapi.units.interfaces.board

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.application.board.CategoryService
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryCreateCommand
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryDto
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryModifyCommand
import kr.dohoonkim.blog.restapi.application.board.dto.CategorySummaryDto
import kr.dohoonkim.blog.restapi.interfaces.board.CategoryController
import kr.dohoonkim.blog.restapi.interfaces.board.dto.ModifyCategoryRequest
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostCategoryRequest
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostedCategory
import kr.dohoonkim.blog.restapi.support.entity.createCategory

internal class CategoryControllerTest: BehaviorSpec({

    val categoryService = mockk<CategoryService>()
    val categoryController = CategoryController(categoryService)
    val category = createCategory()

    beforeSpec {

    }

    Given("카테고리 추가 요청이 주어진다") {
        val request = PostCategoryRequest(category.name)
        val command = CategoryCreateCommand(name = request.name)
        every {categoryService.createCategory(command)} returns CategoryDto.fromEntity(category)

        When("카테고리 생성을 시도하면") {
            val result = categoryController.createCategory(request)

            Then("성공한다") {
                (result is PostedCategory) shouldBe true
                result.id shouldBe category.id
                result.name shouldBe category.name
            }
        }
    }

    Given("카테고리 수정 요청이 주어진다") {
        val request = ModifyCategoryRequest(category.name)
        val command = CategoryModifyCommand(id=category.id, newName = request.name)
        category.changeName(request.name)
        every {categoryService.modifyCategoryName(command)} returns CategoryDto.fromEntity(category)

        When("카테고리 수정을 시도하면") {
            val result = categoryController.updateName(category.id, request)

            Then("성공한다") {
                (result is PostedCategory) shouldBe true
                result.id shouldBe category.id
                result.name shouldBe category.name
            }
        }
    }

    Given("카테고리 삭제 요청이 주어진다") {
        val id = category.id
        When("삭제하면") {
            every { categoryService.deleteCategory(id) } returns Unit
            val result = categoryController.delete(id)
            Then("삭제된다") {
                result shouldBe Unit
            }
        }
    }

    Given("카테고리리 조회 사이즈가 주어진다") {
        val categories = createCategory(40)

        When("전체 카테고리가 조회 사이즈보다 작으면") {
            val size = 50
            every { categoryService.getCategories() } returns categories.map {
                it->CategoryDto.fromEntity(it)
            }
            Then("전체 카테고리가 반환된다") {
                val result = categoryController.getCategories(size)
                result.forEachIndexed { index, postedCategory ->
                    postedCategory.id shouldBe categories[index].id
                    postedCategory.name shouldBe categories[index].name
                }
            }
        }

        When("전체 카테고리가 조회 사이즈보다 크면") {
            val size = 20
            every { categoryService.getCategories() } returns categories.subList(0, size+1).map {
                    it->CategoryDto.fromEntity(it)
            }
            Then("사이즈+1 만큼만 반환된다") {
                val result = categoryController.getCategories(size)
                result.size shouldBe size+1
                result.forEachIndexed { index, postedCategory ->
                    postedCategory.id shouldBe categories[index].id
                    postedCategory.name shouldBe categories[index].name
                }
            }
        }
    }

    afterSpec {
        clearAllMocks()
    }
})