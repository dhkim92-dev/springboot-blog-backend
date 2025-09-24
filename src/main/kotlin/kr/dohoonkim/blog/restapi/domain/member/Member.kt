package kr.dohoonkim.blog.restapi.domain.member

import jakarta.persistence.*
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.generator.UUIDGenerator
import kr.dohoonkim.blog.restapi.domain.BaseEntity
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@Entity(name = "member")
class Member(
    @Id
    @GeneratedValue
    @UuidGenerator(algorithm = UUIDGenerator::class)
    val id: UUID? = null,
    nickname: String = "",
    email: String = "",
    password: String = "",
    isBlocked: Boolean = false,
    oauth2Info: MutableSet<OAuth2Member> = mutableSetOf(),
    refreshTokens: MutableSet<RefreshToken> = mutableSetOf()
) : BaseEntity() {

    @Column(nullable = false, unique = true)
    var nickname: String = nickname
        protected set

    @Column(nullable = false, unique = true)
    var email: String = email
        protected set

    @Column(nullable = false)
    var password: String = password
        protected set

    @Column(nullable = false)
    @ColumnDefault("false")
    var isBlocked: Boolean = isBlocked
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role = Role.MEMBER
        protected set

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = [CascadeType.ALL], orphanRemoval = true)
    val oauth2Info = oauth2Info

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = [CascadeType.ALL], orphanRemoval = true)
    val refreshTokens = refreshTokens

    /**
     * 회원 비밀번호를 업데이트 합니다.
     * 관리자 권한이 있거나, 본인 계정이어야 합니다.
     * @param updater 업데이트를 시도하는 회원
     * @param password 새로운 비밀번호, 인코딩 된 상태여야합니다.
     * @throws ForbiddenException 권한이 없는 경우
     */
    fun updatePassword(updater: Member, password: String) {
        if ( !updater.isAdmin() || this != updater) {
            throw ForbiddenException(ErrorCodes.RESOURCE_OWNERSHIP_VIOLATION)
        }

        if ( this.isBlocked ) {
            throw ForbiddenException(ErrorCodes.NO_PERMISSION)
        }

        this.password = password // passwordEncoder.encode(password);
    }

    /**
     * 회원 닉네임을 업데이트 합니다.
     * 관리자 권한이 있거나, 본인 계정이어야 합니다.
     * @param updater 업데이트를 시도하는 회원
     * @param nickname 새로운 닉네임
     * @throws ForbiddenException 권한이 없는 경우
     */
    fun updateNickname(updater: Member, nickname: String) {
        if ( !updater.isAdmin() || this != updater) {
            throw ForbiddenException(ErrorCodes.RESOURCE_OWNERSHIP_VIOLATION)
        }

        if ( this.isBlocked ) {
            throw ForbiddenException(ErrorCodes.NO_PERMISSION)
        }

        if ( this.nickname == nickname ) return;
        this.nickname = nickname
    }

    /**
     * 회원을 차단합니다.
     * 관리자 권한이 있어야 합니다.
     * @param requester 차단을 시도하는 회원
     * @throws ForbiddenException 권한이 없는 경우
     */
    fun blockMember(requester: Member) {
        if ( !requester.isAdmin() ) {
            throw ForbiddenException(ErrorCodes.NO_PERMISSION)
        }

        this.isBlocked = true
    }

    /**
     * 회원 탈퇴를 합니다.
     * 본인 계정에 대해서만 가능합니다.
     * 단 회원이 차단된 상태라면 탈퇴가 불가능합니다.
     * 소프트 딜리트로 처리됩니다.
     * @param requester 탈퇴를 시도하는 회원
     * @throws ForbiddenException 권한이 없는 경우
     */
    fun withdrawMember(requester: Member) {
        if (this.isBlocked) {
            throw ForbiddenException(ErrorCodes.BLOCKED_MEMBER_CAN_NOT_WITHDRAW)
        }

        if (this != requester) {
            throw ForbiddenException(ErrorCodes.RESOURCE_OWNERSHIP_VIOLATION)
        }
        this.refreshTokens.clear()
        this.oauth2Info.clear()
        this.markAsDeleted()
    }

    /**
     * Admin 권한을 확인하빈다.
     * @return 관리자 권한이 있으면 true
     */
    fun isAdmin(): Boolean {
        return this.role == Role.ADMIN
    }

    fun linkOAuth2Info(info: OAuth2Member) {
        this.oauth2Info.add(info)
        info.linkMember(this)
    }

    /**
     * 발급받은 RefreshToken을 저장합니다.
     * @param token 발급받은 리프레시 토큰
     * @param expireAt 토큰 만료 시간
     */
    fun addRefreshToken(token: String, expireAt: Instant) {
        val refreshToken = RefreshToken(
            member = this,
            token = token,
            expireAt = expireAt
        )

        if ( this.refreshTokens.size > 5 ) {
            // 가장 오래된 토큰을 삭제합니다.
            while (this.refreshTokens.size > 4) {
                val oldestToken = this.refreshTokens.minByOrNull { it.createdAt }!!
                this.refreshTokens.remove(oldestToken)
            }
        }

        this.refreshTokens.add(refreshToken)
    }

    /**
     * 로그아웃 시 해당 RefreshToken을 삭제합니다.
     * @param refreshToken 삭제할 리프레시 토큰
     */
    fun logout(refreshToken: String) {
        this.refreshTokens.filter { it.isExpired() }
            .forEach {
                token -> this.refreshTokens.remove(token)
            }

        this.refreshTokens.filter { it.token == refreshToken }
            .forEach { token -> this.refreshTokens.remove(token) }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Member) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}