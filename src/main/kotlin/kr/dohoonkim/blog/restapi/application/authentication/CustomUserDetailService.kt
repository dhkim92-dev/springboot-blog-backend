package kr.dohoonkim.blog.restapi.application.authentication

import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.domain.member.CustomUserDetails
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import kr.dohoonkim.blog.restapi.security.jwt.JwtAuthentication
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsPasswordService
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

//@Service
class CustomUserDetailService(
    private val memberRepository: MemberRepository,
): UserDetailsService {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    /**
     * 사용자 이메일을 받아 Member를 조회하고
     * 조회된 Member를 JwtAuthentication으로 변환하여 반환한다
     */
    override fun loadUserByUsername(email: String): UserDetails {
        val member = memberRepository.findByEmail(email)

        return member?.let { JwtAuthentication.fromMember(it) }
            ?: throw NotFoundException(ErrorCodes.MEMBER_NOT_FOUND)
    }

    // 불필요한 인터페이스 구현
//    override fun updatePassword(user: UserDetails, newPassword: String): UserDetails {
//        val member = memberRepository.findByEmail(user.username)
//            ?: throw NotFoundException(ErrorCodes.MEMBER_NOT_FOUND)
//
//
//        return member.let { CustomUserDetails.from(member) }
//    }
}