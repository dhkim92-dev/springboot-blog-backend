package kr.dohoonkim.blog.restapi.units.domain.board.entity

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.dohoonkim.blog.restapi.support.entity.createArticle
import kr.dohoonkim.blog.restapi.support.entity.createCategory
import kr.dohoonkim.blog.restapi.support.entity.createMember
import java.time.LocalDateTime

internal class ArticleTest: BehaviorSpec({

    Given("게시글이 주어진다") {
        val article = createArticle(createMember(), createCategory())

        When("본문을 변경하면") {
            val newContent="newContent"
            article.updateContents(newContent)

            Then("변경 된다") {
                article.contents shouldBe newContent
            }
        }

        When("본문에 Null이 입력되면") {
            val original = article.contents
            article.updateContents(null)
            Then("변경되지 않는다") {
                 article.contents shouldBe original
            }
        }

        When("카테고리를 변경하면") {
            val newCategory = createCategory(2)[1]
            article.updateCategory(newCategory)

            Then("변경된다"){
                article.category.id shouldBe newCategory.id
            }
        }

        When("카테고리가 null로 변경하면") {
            val curCategory = article.category
            article.updateCategory(null)
            Then("변겨오디지 않는다") {
                article.category.id shouldBe curCategory.id
            }
        }

        When("제목을 변경하면") {
            val newTitle = "newTitle"
            article.updateTitle(newTitle)

            Then("변경된다") {
                article.title shouldBe newTitle
            }
        }

        When("제목을 null로 변경하면") {
            val original = article.title
            article.updateTitle(null)
            Then("변경되지 않는다") {
                article.title shouldBe original
            }
        }

        When("최근 수정 시각 업데이트 호출을 하면") {
            val cur = LocalDateTime.now()
            article.updateLastModifiedDate()

            Then("최근 수정 시각이 변경된다") {
                article.updatedAt shouldBeGreaterThan cur
            }
        }
    }
})