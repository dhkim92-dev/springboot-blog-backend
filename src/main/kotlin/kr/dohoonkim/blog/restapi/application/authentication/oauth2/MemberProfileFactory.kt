package kr.dohoonkim.blog.restapi.application.authentication.oauth2

import kr.dohoonkim.blog.restapi.application.authentication.dto.MemberProfile

interface MemberProfileFactory {

    fun build(attributes: MutableMap<String, Any>): MemberProfile;
}