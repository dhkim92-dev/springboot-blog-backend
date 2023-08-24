package kr.dohoonkim.blog.restapi.domain.member

import jakarta.persistence.*
import kr.dohoonkim.blog.restapi.common.entity.UuidPrimaryKeyEntity
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle
import org.hibernate.annotations.ColumnDefault
import java.util.*

@Entity(name="member")
class Member : UuidPrimaryKeyEntity {
    @Column(nullable = false, unique = true)
    var nickname : String
        protected set

    @Column(nullable = false, unique = true)
    var email : String
        protected set

    @Column(nullable = false)
    var password : String
        protected  set

    @Column(nullable = false)
    @ColumnDefault("false")
    var isActivated : Boolean

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role : Role = Role.MEMBER

    fun updateEmail(email : String) {
        this.email = email;
    }

    fun updatePassword(password : String){
        this.password = password // passwordEncoder.encode(password);
    }

    fun updateNickname(nickname : String) {
        this.nickname = nickname
    }

    fun updateRole(role : Role) {
        this.role = role;
    }

    constructor(id : UUID) {
        this.id = id
        this.nickname=""
        this.email = ""
        this.password = ""
        this.isActivated = false
    }

    constructor(nickname : String, email : String, password : String, isActivated : Boolean?) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.isActivated = isActivated ?: false;
    }

    override fun toString(): String {
        return ToStringBuilder
                .reflectionToString(this, ToStringStyle.JSON_STYLE)
    }

}