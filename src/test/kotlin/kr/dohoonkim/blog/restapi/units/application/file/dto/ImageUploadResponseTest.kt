package kr.dohoonkim.blog.restapi.units.application.file.dto

import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.file.dto.ImageUploadResponse
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation
import java.util.*

class ImageUploadResponseTest: DtoValidation() {
    init {
        Given("생성자 입력 값이 주어진다") {
            val url = "https://www.dohoon-kim.kr/resource/${UUID.randomUUID()}"
            When("생성자를 호출하면") {
                val result = ImageUploadResponse(url)
                Then("객체가 생성된다") {
                    result.url shouldBe url
                }
            }
        }
    }
}