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
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService(private val memberRepository: MemberRepository) : UserDetailsService, UserDetailsPasswordService{
    private val log : Logger = LoggerFactory.getLogger(javaClass)

    override fun loadUserByUsername(email : String): UserDetails {
        val member = this.memberRepository.findByEmail(email)

        return member?.let{ CustomUserDetails.from(member) }
                ?: throw EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND)
    }

    override fun updatePassword(user: UserDetails, newPassword: String) : UserDetails {
        val member = this.memberRepository.findByEmail(user.username)?:throw EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND)
        member.updatePassword(newPassword)
        this.memberRepository.save(member)
        return member.let{ CustomUserDetails.from(member)}
    }
}