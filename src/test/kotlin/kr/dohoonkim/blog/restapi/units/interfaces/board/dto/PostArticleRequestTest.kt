package kr.dohoonkim.blog.restapi.units.interfaces.board.dto

import io.kotest.core.spec.style.scopes.GivenScope
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostArticleRequest
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation
import org.hibernate.validator.constraints.Length

class PostArticleRequestTest: DtoValidation() {

    private val titleBlank = "제목이 주어져야합니다."
    private val titleLength = "제목은 최소 1글자 최대 255글자 입니다."
    private val contentsBlank =  "본문 내용이 주어져야합니다."
    private val categoryBlank = "카테고리 명이 입력되어야 합니다"
    private val categoryLength = "카테고리 명은 최소 1글자 최대 16글자입니다."

    init {
        Given("정상 폼이 주어진다") {
            val request = PostArticleRequest(
                title = "title",
                contents = "contents",
                category = "category"
            )

            When("검증하면"){
                val result = validator.validate(request)
                Then("에러가 발생하지 않는다") {
                    result.isEmpty() shouldBe true
                    request.title shouldBe "title"
                    request.contents shouldBe "contents"
                    request.category shouldBe "category"
                }
            }
        }

        Given("제목이 공백으로 주어진다") {
            val request = PostArticleRequest(
                title = "",
                contents = "contents",
                category = "category"
            )
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find { it.message == titleBlank } shouldNotBe null
                }
            }
        }

        Given("길이를 만족하지 않는 제목이 주어진다") {
            val sb = StringBuffer()
            repeat(256) {
                sb.append("a")
            }
            val request = PostArticleRequest(
                title = sb.toString(),
                contents = "contents",
                category = "category"
            )
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find { it.message == titleLength } shouldNotBe null
                }
            }
        }

        Given("본문이 공백으로 주어진다") {
            val request = PostArticleRequest(
                title = "title1",
                contents = "",
                category = "category"
            )
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find { it.message == contentsBlank } shouldNotBe null
                }
            }
        }

        Given("카테고리 명이 공백으로 주어진다") {
            val request = PostArticleRequest(
                title = "title",
                contents = "contents",
                category = ""
            )
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find { it.message == categoryBlank } shouldNotBe null
                }
            }
        }

        Given("길이를 만족하지 않는 카테고리 명이 주어진다") {
            val request = PostArticleRequest(
                title = "title",
                contents = "contents",
                category = "categoryyyyyyyyyyyyyyyyy"
            )
            When("검증하면") {
                val result = validator.validate(request)
                Then("에러가 발생한다") {
                    result.isEmpty() shouldBe false
                    result.find { it.message == categoryLength } shouldNotBe null
                }
            }
        }
    }
}