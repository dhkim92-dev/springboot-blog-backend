package kr.dohoonkim.blog.restapi.units.domain.board.entity

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import kr.dohoonkim.blog.restapi.domain.board.Category
import kr.dohoonkim.blog.restapi.support.entity.createArticle
import kr.dohoonkim.blog.restapi.support.entity.createCategory
import kr.dohoonkim.blog.restapi.support.entity.createMember

internal class CategoryTest(): BehaviorSpec({

    var category=Category(0L)

    beforeContainer {
        category = createCategory()
    }

    Given("카테고리 이름이 주어진다") {
        val categoryName="category"

        When("이름을 주고 카테고리를 생성하면") {
            val namedCategory = Category(categoryName)
            Then("ID 가 0인 카테고리가 만들어진다") {
                namedCategory.id shouldBe 0L
                namedCategory.name shouldBe categoryName
            }
        }

        When("이름을 변경하면") {
            category.changeName(categoryName)
            Then("변경된다") {
                println("check name")
                category.name shouldBe categoryName
            }
        }

        When("업데이트 시각을 변경하면") {
            val original = category.updatedAt
            category.updateLastModifiedDate()
            Then("변경된다") {
                category.updatedAt shouldBeGreaterThan original
            }
        }
    }

    Given("추가할 새로운 게시글이 주어진다") {
        val member = createMember()
        val article = createArticle(member, Category(0L))
        When("카테고리에 게시글을 추가하면") {
            category.addArticle(article)
            Then("추가된다") {
                val categoryArticle = category.articles.find{it.category.id == category.id}
                categoryArticle shouldNotBe null
                categoryArticle?.id shouldBe article.id
                categoryArticle?.category?.id shouldBe category.id
            }
        }
    }

    afterEach {
        clearAllMocks()
    }
})