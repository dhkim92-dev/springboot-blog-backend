package kr.dohoonkim.blog.restapi.units.config

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.swagger.v3.oas.models.OpenAPI
import kr.dohoonkim.blog.restapi.config.SwaggerConfig

internal class SwaggerConfigTest: AnnotationSpec() {

    private val swaggerConfig = SwaggerConfig()

    @Test
    fun `openAPI를 호출하면 OpenAPI 객체가 반환된다`() {
        (swaggerConfig.openAPI() is OpenAPI) shouldBe true
    }
}