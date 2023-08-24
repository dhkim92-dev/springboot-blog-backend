package kr.dohoonkim.blog.restapi.units.application.board

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryCreateDto
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryDto
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryModifyDto
import kr.dohoonkim.blog.restapi.application.board.impl.CategoryServiceImpl
import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.exceptions.ConflictException
import kr.dohoonkim.blog.restapi.common.error.exceptions.EntityNotFoundException
import kr.dohoonkim.blog.restapi.domain.article.Category
import kr.dohoonkim.blog.restapi.domain.article.repository.CategoryRepository
import java.util.*

class CategoryServiceTest : BehaviorSpec() {

    private val categoryRepository = mockk<CategoryRepository>()

    private val categoryService = CategoryServiceImpl(categoryRepository)

    private lateinit var category1 : Category

    private lateinit var category2 : Category

    init {

        beforeTest {
            category1 = Category("test-category-1")
            category2 = Category("test-category-2")
        }

        Given("새로운 카테고리를 추가하려한다.") {
            every {categoryRepository.save(any())} returns category1
            When("신규 카테고리를 생성하여 저장하면") {
                every {categoryRepository.existsByName(any()) } returns false
                Then("정상적으로 저장되어야한다.") {
                    val dto = CategoryCreateDto("test-category-1")
                    val ret = categoryService.createCategory(dto)
                    ret.name shouldBe  "test-category-1"
                    ret.count shouldBe 0L
                }
            }

            When("중복된 카테고리를 저장하려하면") {
                every {categoryRepository.existsByName(any()) } returns true
                Then("에러가 발생해야한다.") {
                    shouldThrow<ConflictException> {
                        val dto = CategoryCreateDto("test-category-1")
                        categoryService.createCategory(dto)
                    }
                }
            }
        }

        Given("카테고리 명을 수정한다.") {

            When("수정하려는 카테고리 이름이 존재하지 않는다면") {
                every {categoryRepository.findById(any())} returns Optional.of(category1)
                every {categoryRepository.existsByName(any())} returns false
                every {categoryRepository.save(any())} returns Category(name="test-category-3")
                Then("정상적으로 수정된다.") {
                    val dto = CategoryModifyDto(
                        id = 0L,
                        newName = "test-category-3"
                    )
                    val modifiedCategory = categoryService.modifyCategoryName(dto)

                    modifiedCategory.name shouldBe "test-category-3"
                }
            }

            When("수정하려는 카테고리 이름이 존재한다면") {
                every {categoryRepository.findById(any())} returns Optional.of(category1)
                every {categoryRepository.existsByName(any())} returns true
                every {categoryRepository.save(any())} returns Category("test-category-3")

                Then("에러가 발생한다.") {
                    shouldThrow<ConflictException> {
                        categoryService.modifyCategoryName(CategoryModifyDto(id=0L, newName="test-category-3"))
                    }
                }
            }

            When("존재하지 않는 카테고리의 이름을 수정하려 한다면") {
                every {categoryRepository.findById(any())} throws EntityNotFoundException(ErrorCode.CATEGORY_NOT_FOUND)
                Then("에러가 발생한다.") {
                    shouldThrow<EntityNotFoundException> {
                        categoryService.modifyCategoryName(CategoryModifyDto(id = 0L, newName = "test-category-3"))
                    }
                }
            }
        }

        Given("카테고리를 삭제한다.") {
            When("카테고리를 삭제하면") {
                every{categoryRepository.deleteById(0L)} returns Unit
                Then("카테고리가 삭제된다.") {
                    shouldNotThrowAny {
                        categoryService.deleteCategory(0L)
                    }
                }
            }
        }

        Given("카테고리 목록을 조회한다.") {
            When("세개의 카테고리와 가각 1개, 3개, 0개의 게시글이 있을 때") {
                every {categoryRepository.findAllCategory()} returns listOf(
                    CategoryDto(category1.id!!, category1.name, 1),
                    CategoryDto(category2.id!!, category2.name, 3),
                    CategoryDto(3L, "test-category-3", 0)
                )

                Then("세개의 카테고리가 각각 1, 3, 0개의 게시글 수가 포함되어 반환된다.") {
                    val categories = categoryService.getCategories()

                    categories.size shouldBe 3
                    categories[0].count shouldBe 1
                    categories[0].name shouldBe category1.name
                    categories[1].count shouldBe 3
                    categories[1].name shouldBe category2.name
                    categories[2].count shouldBe 0
                    categories[2].name shouldBe "test-category-3"
                }
            }
        }

        afterTest {
            clearAllMocks()
        }
    }

    fun createCategory() = Category(
        name = UUID.randomUUID().toString()
    )

}