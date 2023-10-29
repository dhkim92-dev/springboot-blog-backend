package kr.dohoonkim.blog.restapi.application.authentication.oauth2

import kr.dohoonkim.blog.restapi.application.authentication.dto.GoogleMemberProfile
import kr.dohoonkim.blog.restapi.application.authentication.dto.MemberProfile

class GoogleMemberProfileFactory : MemberProfileFactory{

    override fun build(attributes: MutableMap<String, Any>): MemberProfile {
        return GoogleMemberProfile(attributes)
    }
}