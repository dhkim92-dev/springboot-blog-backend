package kr.dohoonkim.blog.restapi.application.member.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import kr.dohoonkim.blog.restapi.domain.member.Member
import java.util.UUID

data class MemberDto(
        val id : UUID,
        val nickname : String,
        val email : String,
        @JsonIgnore
        val role : String,
        @JsonProperty("is_activated")
        val isActivated : Boolean
){

    companion object {
        fun fromEntity(member : Member) : MemberDto {
            return MemberDto(
                id = member.id,
                nickname = member.nickname,
                email = member.email,
                role = member.role.rolename,
                isActivated = member.isActivated);
        }
    }
}
