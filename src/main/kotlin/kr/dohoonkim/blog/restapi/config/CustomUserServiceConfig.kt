package kr.dohoonkim.blog.restapi.config

import kr.dohoonkim.blog.restapi.application.authentication.CustomUserDetailService
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class CustomUserServiceConfig(private val memberRepository: MemberRepository) {
    @Bean
    fun passwordEncrypt(): PasswordEncoder {
        return BCryptPasswordEncoder(10)
    }

    @Bean
    fun customUserDetailService(): CustomUserDetailService {
        return CustomUserDetailService(memberRepository, passwordEncrypt())
    }
}