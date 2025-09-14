package kr.dohoonkim.blog.restapi.units.application.authentication.exceptions

import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.security.oauth2.exceptions.NotSupportedOAuth2ProviderException
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.support.dto.DtoValidation

internal class NotSupportedOAuth2ProviderExceptionTest: DtoValidation() {

    init {
        Given("예외 객체가 주어진다") {
            val exception = NotSupportedOAuth2ProviderException()
            When("메시지와 스테이터스를 확인하면") {
                Then("ErrorCode.NOT_SUPPORTED_OAUTH2_PROVIDER 와 동일하다") {
                    exception.message shouldBe ErrorCodes.NOT_SUPPORTED_OAUTH2_PROVIDER.message
                    exception.status shouldBe ErrorCodes.NOT_SUPPORTED_OAUTH2_PROVIDER.status
                    exception.code shouldBe ErrorCodes.NOT_SUPPORTED_OAUTH2_PROVIDER.code
                }
            }
        }
    }
}