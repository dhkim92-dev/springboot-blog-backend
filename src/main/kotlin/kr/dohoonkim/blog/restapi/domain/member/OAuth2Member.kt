package kr.dohoonkim.blog.restapi.domain.member

import jakarta.persistence.*
import kr.dohoonkim.blog.restapi.security.oauth2.OAuth2Provider
import java.util.*

@Entity(name="oauth2_member")
class OAuth2Member(
    @Id
    val id: UUID = UUID.randomUUID(),
    @Enumerated(EnumType.STRING)
    var provider: OAuth2Provider = OAuth2Provider.GITHUB,
    @Column
    var userId: String = "",
    @Column
    var email: String = "",
){
    @Column
    var accessToken: String = ""

    @ManyToOne
    @JoinColumn(name="member_id")
    var member: Member = Member(UUID.randomUUID())
}