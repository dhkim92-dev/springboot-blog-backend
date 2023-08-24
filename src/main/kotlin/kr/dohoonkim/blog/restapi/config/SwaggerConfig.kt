package kr.dohoonkim.blog.restapi.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI() : OpenAPI {
        val info : Info = Info()
                .version("1.0.0")
                .title("dohoon-kim.kr API")
                .description("개인 블로그 API")

        val jwt = "Jwt auth"
        val securityRequirement : SecurityRequirement = SecurityRequirement().addList(jwt)
        val securityScheme = SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .name(HttpHeaders.AUTHORIZATION)
                .`in`(SecurityScheme.In.HEADER)

        val components : Components = Components().addSecuritySchemes(jwt,
                securityScheme
        )

        return OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components)
    }
}