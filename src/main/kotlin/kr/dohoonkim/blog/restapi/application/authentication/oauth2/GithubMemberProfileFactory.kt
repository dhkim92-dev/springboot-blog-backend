package kr.dohoonkim.blog.restapi.application.authentication.oauth2

import kr.dohoonkim.blog.restapi.application.authentication.dto.GithubMemberProfile
import kr.dohoonkim.blog.restapi.application.authentication.dto.MemberProfile

class GithubMemberProfileFactory : MemberProfileFactory {
    override fun build(attributes: MutableMap<String, Any>): MemberProfile {
        return GithubMemberProfile(attributes)
    }
}