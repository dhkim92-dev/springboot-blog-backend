package kr.dohoonkim.blog.restapi.domain.member

enum class Role(val rolename: String) {
    ADMIN("ROLE_ADMIN"),
    MEMBER("ROLE_MEMBER"),
    GUEST("ROLE_GUEST");

    companion object {

        fun from(rolename: String): Role {
            return when (rolename) {
                "ROLE_ADMIN" -> ADMIN
                "ROLE_MEMBER" -> MEMBER
                else -> GUEST
            }
        }
    }
}