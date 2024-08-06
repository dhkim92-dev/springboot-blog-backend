package kr.dohoonkim.blog.restapi.application.authentication.vo

import kr.dohoonkim.blog.restapi.domain.member.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User
import java.util.*
import kotlin.collections.HashMap


//TODO 어트리뷰트 분석이 제대로 되질 않았음
// 추후 Github 응답 필드 제대로 매핑

/**
 * MemberProfile class
 * OAuth2 인증 시 얻어온 사용자 정보로부터 사용할 field를 저장한다.
 * @author dhkim92.dev@gmail.com
 * @since 2024.01.10
 * @property customAuthorities
 * @property email
 * @property nickname
 * @property customAuthorities
 */
open class MemberProfile(private val customAttributes: MutableMap<String, Any>) : OAuth2User {
    var email: String = ""
    var nickname: String = ""
    var customAuthorities = mutableListOf<GrantedAuthority>()

    override fun getAttributes(): MutableMap<String, Any> {
        return customAttributes
    }

    override fun getName(): String {
        return nickname
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return customAuthorities
    }
}