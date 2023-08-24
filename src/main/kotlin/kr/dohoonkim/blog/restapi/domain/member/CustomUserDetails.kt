package kr.dohoonkim.blog.restapi.domain.member

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

class CustomUserDetails
    private constructor(
        var memberId : UUID,
        var email : String,
        var nickname : String,
        private var password : String,
        var role : Role,
        var isActivated : Boolean
    ) : UserDetails {

    companion object {
        fun from(member : Member) : CustomUserDetails {
            return CustomUserDetails(
                    member.id,
                    member.email,
                    member.nickname,
                    member.password,
                    member.role,
                    member.isActivated
            )
        }
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(this.role.rolename))
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return this.email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return this.isActivated
    }

}