package kr.dohoonkim.blog.restapi.units.common.response

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.common.response.ApiResult
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import org.springframework.http.HttpStatus

internal class ApiResultTest: AnnotationSpec() {

    @Test
    fun `Ok에 응답 코드와 데이터를 넣으면 ApiResult 객체가 반환된다`() {
        val result = System.currentTimeMillis()
        val wrapper = ApiResult.Ok(ResultCode.HEALTH_CHECK_SUCCESS, result)

        wrapper.status shouldBe  HttpStatus.OK.value()
        wrapper.code shouldBe ResultCode.HEALTH_CHECK_SUCCESS.code
        wrapper.data shouldBe result
        wrapper.message shouldBe ResultCode.HEALTH_CHECK_SUCCESS.message
    }
}