package kr.dohoonkim.blog.restapi.application.authentication

import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.exceptions.EntityNotFoundException
import kr.dohoonkim.blog.restapi.domain.member.CustomUserDetails
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsPasswordService
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

//@Service
class CustomUserDetailService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
): UserDetailsService, UserDetailsPasswordService {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("CustomUserDetailService passwordEncoder : ${passwordEncoder}")
    }

    override fun loadUserByUsername(email: String): UserDetails {
        val member = memberRepository.findByEmail(email)

        return member?.let { CustomUserDetails.from(member) }
            ?: throw EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND)
    }

    override fun updatePassword(user: UserDetails, newPassword: String): UserDetails {
        val member = memberRepository.findByEmail(user.username)
            ?: throw EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND)

        member.updatePassword(passwordEncoder.encode(newPassword))
        memberRepository.save(member)

        return member.let { CustomUserDetails.from(member) }
    }
}