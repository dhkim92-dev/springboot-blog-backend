package kr.dohoonkim.blog.restapi.domain.member

import jakarta.persistence.Embeddable

@Embeddable
class Photo(
    val defaultImage: String,
    val iconImage: String
) {
}