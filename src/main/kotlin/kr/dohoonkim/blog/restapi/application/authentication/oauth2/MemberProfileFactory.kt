package kr.dohoonkim.blog.restapi.application.authentication.oauth2

import kr.dohoonkim.blog.restapi.application.authentication.vo.MemberProfile

interface MemberProfileFactory {

    fun build(attributes: MutableMap<String, Any>): MemberProfile;
}