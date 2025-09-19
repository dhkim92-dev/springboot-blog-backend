package kr.dohoonkim.blog.restapi.domain.member

import jakarta.persistence.*
import kr.dohoonkim.blog.restapi.common.generator.UUIDGenerator
import org.hibernate.annotations.UuidGenerator
import java.util.*

@Entity(name="oauth2_member")
class OAuth2Member(
    @Id
    @GeneratedValue
    @UuidGenerator(algorithm = UUIDGenerator::class)
    val id: UUID? = null,
    @Enumerated(EnumType.STRING)
    var provider: OAuth2Provider = OAuth2Provider.GITHUB,
    @Column
    var userId: String = "",
    @Column
    var email: String = "",
) {
    @ManyToOne
    @JoinColumn(name="member_id")
    private var member: Member = Member()

    fun linkMember(member: Member) {
        this.member = member
    }
}