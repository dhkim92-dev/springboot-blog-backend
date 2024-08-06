package kr.dohoonkim.blog.restapi.units.application.file.exceptions

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.file.exceptions.NotSupportImageTypeException
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes.NOT_SUPPORT_IMAGE_TYPE

class NotSupportImageTypeExceptionTest: BehaviorSpec({

    Given("예외 객체가 주어진다") {
        val exception = NotSupportImageTypeException()
        When("상태 코드와 메시지를 확인하면") {
            Then("NOT_SUPPORTED_IMAGE_TYPE가 동일하다") {
                exception.message shouldBe NOT_SUPPORT_IMAGE_TYPE.message
                exception.code shouldBe NOT_SUPPORT_IMAGE_TYPE.code
                exception.status shouldBe NOT_SUPPORT_IMAGE_TYPE.status
            }
        }
    }
})