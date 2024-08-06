package kr.dohoonkim.blog.restapi.units.application.board

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryCreateCommand
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryDto
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryModifyCommand
import kr.dohoonkim.blog.restapi.application.board.impl.CategoryServiceImpl
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes.ALREADY_EXIST_CATEGORY
import kr.dohoonkim.blog.restapi.common.error.exceptions.ConflictException
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.domain.board.Category
import kr.dohoonkim.blog.restapi.domain.board.repository.CategoryRepository
import kr.dohoonkim.blog.restapi.support.entity.createCategory
import org.springframework.data.repository.findByIdOrNull

internal class CategoryServiceTest: BehaviorSpec({

    val categoryRepository = mockk<CategoryRepository>()
    val categoryService = CategoryServiceImpl(categoryRepository)
    val categories = createCategory(2)

    beforeSpec {

    }

    fun checkEqual(dto: CategoryDto, entity: Category) {
        dto.id shouldBe entity.id
        dto.name shouldBe entity.name
        dto.count shouldBe entity.articles.size
    }

    Given("카테고리 생성 커맨드가 주어진다") {
        val command = CategoryCreateCommand(name = categories[0].name)
        When("이미 존재하는 카테고리이면") {
            every { categoryRepository.existsByName(any()) } returns true
            Then("ConflictException이 발생한다") {
                shouldThrow<ConflictException> {
                    categoryService.createCategory(command)
                }.message shouldBe ALREADY_EXIST_CATEGORY.message
            }
        }

        When("정상 요청이면") {
            every { categoryRepository.existsByName(any()) } returns false
            every { categoryRepository.save(any()) } returns categories[0]
            Then("Category가 생성된다") {
                val result = categoryService.createCategory(command)
                checkEqual(result, categories[0])
            }
        }
    }

    Given("카테고리 수정 커맨드가 주어진다") {
        val command = CategoryModifyCommand(id = categories[0].id, newName = "new-category-name")
        When("존재하지 않는 카테고리 ID라면") {
            every { categoryRepository.findByIdOrNull(any()) } returns null
            Then("NotFoundException이 발생한다") {
                shouldThrow<NotFoundException> {
                    categoryService.modifyCategoryName(command)
                }.message shouldBe ErrorCodes.CATEGORY_NOT_FOUND.message
            }
        }

        When("변경하려는 이름이 이미 존재할 경우") {
            every { categoryRepository.findByIdOrNull(any()) } returns categories[0]
            every { categoryRepository.existsByName(any()) } returns true

            Then("ConflictException이 발생한다") {
                shouldThrow<ConflictException> {
                    categoryService.modifyCategoryName(command)
                }.message shouldBe ALREADY_EXIST_CATEGORY.message
            }
        }

        When("정상 요청일 경우") {
            every { categoryRepository.findByIdOrNull(any()) } returns categories[0]
            every { categoryRepository.existsByName(any()) } returns false
            Then("카테고리 이름이 변경된다") {
                val result = categoryService.modifyCategoryName(command)
                checkEqual(result, categories[0])
            }
        }
    }

    Given("삭제하려는 카테고리 ID가 주어진다") {
        When("삭제요청을 하면") {
            every { categoryRepository.deleteById(any()) } returns Unit
            Then("삭제된다") {
                shouldNotThrowAny {
                    categoryService.deleteCategory(categoryId = categories[0].id)
                }
            }
        }
    }

    Given("카테고리 목록을 조회한다") {
        When("조회하면") {
            every {categoryRepository.findAllCategory()} returns categories.map { it -> CategoryDto.fromEntity(it) }
            Then("카테고리 리스트가 반환된다") {
                val result = categoryService.getCategories()
                result.isEmpty() shouldBe false
                result.forEachIndexed{ i, v ->  checkEqual(v, categories[i]) }
            }
        }
    }

    afterSpec {
        clearAllMocks()
    }
})