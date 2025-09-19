package kr.dohoonkim.blog.restapi.config

//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.http.HttpMethod.*
//import org.springframework.web.cors.CorsConfiguration
//import org.springframework.web.cors.CorsConfigurationSource
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource
//
//@Configuration
//class CorsConfig(
//    @Value("\${cors.host}") private var host: String,
//    @Value("\${cors.frontend}") private var frontend: String
//) {
//
//    @Bean
//    fun corsConfigurationSource(): CorsConfigurationSource {
//        val corsConfiguration = CorsConfiguration()
//        corsConfiguration.allowCredentials = true
//        corsConfiguration.allowedHeaders = listOf("Content-Type", "Authorization", "Cache-Control")//, "Access-Control-Allow-Origin")
//        corsConfiguration.allowedMethods = listOf("GET", "HEAD", "OPTION", "PUT", "PATCH", "POST", "DELETE")
//        corsConfiguration.allowedOrigins = listOf(host, frontend)
//        val source = UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfiguration)
//
//        return source;
//    }
//}
