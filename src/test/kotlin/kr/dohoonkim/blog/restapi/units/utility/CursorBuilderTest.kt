package kr.dohoonkim.blog.restapi.units.utility

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.common.utility.CursorListBuilder
import java.time.LocalDateTime


class CursorBuilderTest : BehaviorSpec({
    Given("커서를 생성할 데이터가 주어지고") {
        val data = listOf(
            TestEntity(name = "tester01", createdAt = LocalDateTime.now()),
            TestEntity(name = "tester02", createdAt = LocalDateTime.now()),
            TestEntity(name = "tester03", createdAt = LocalDateTime.now())
        )

        When("createdAt, name을 커서로하는 next 커서를 생성하려 할 때") {
            Then("createdAt, name, direction을 포함한 next 커서가 정상적으로 반환된다.") {
                val cursor = CursorListBuilder.next(
                    data,
                    listOf("createdAt", "name"),
                    2L,
                    false
                )

                println("next url : $cursor")
                cursor!!.contains("createdAt") shouldBe true
                cursor!!.contains(data[data.size-1].createdAt.toString()) shouldBe true
                cursor!!.contains("name") shouldBe true
            }
        }

        When("createdAt, name을 커서로하는 prev 커서를 생성하려 할 때") {
            Then("createdAt, name, direction을 포함한 prev 커서가 정상적으로 반환된다.")  {
                val cursor = CursorListBuilder.prev(data,
                    listOf("createdAt", "name"),
                    false
                )

                println("next url : $cursor")
                cursor!!.contains("createdAt") shouldBe true
                cursor!!.contains(data[0].createdAt.toString()) shouldBe true
                cursor!!.contains("name") shouldBe true
            }
        }
    }
}) {

    class TestEntity(
        val name : String,
        val createdAt : LocalDateTime
    )

}