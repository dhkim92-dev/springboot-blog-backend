package kr.dohoonkim.blog.restapi.application.authentication.impl

import jakarta.transaction.Transactional
import kr.dohoonkim.blog.restapi.application.authentication.jwt.JwtService
import kr.dohoonkim.blog.restapi.application.authentication.usecases.RevokeRefreshTokenUseCase
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class RevokeRefreshTokenUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val jwtService: JwtService
): RevokeRefreshTokenUseCase {

    @Transactional
    override fun revoke(refreshToken: String) {
        val decodedJWT = jwtService.decodeRefreshToken(refreshToken)
        val memberId = UUID.fromString(decodedJWT.subject)
        val member = memberRepository.findByIdOrNull(memberId)
            ?: return;
        member.logout(refreshToken)
        memberRepository.save(member)
    }
}