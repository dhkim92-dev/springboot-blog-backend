package kr.dohoonkim.blog.restapi.units.config

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.config.CorsConfig
import org.springframework.web.cors.CorsConfigurationSource


internal class CorsConfigTest: AnnotationSpec() {

    val host = "http://127.0.0.1:8080"
    val frontend = "http://127.0.0.1:3000"
    private val corsConfig = CorsConfig(host, frontend)

    @Test
    fun `corsConfigurationSource를 호출하면 객체가 반환된다`() {
        val source = corsConfig.corsConfigurationSource()
        (source is CorsConfigurationSource) shouldBe  true
    }
}