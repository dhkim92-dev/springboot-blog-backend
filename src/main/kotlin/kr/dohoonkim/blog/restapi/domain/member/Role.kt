package kr.dohoonkim.blog.restapi.domain.member

enum class Role(val rolename: String) {
    ADMIN("ROLE_ADMIN"),
    MEMBER("ROLE_MEMBER");

    companion object {
        fun from(rolename: String): Role {
            when (rolename) {
                "ROLE_ADMIN" -> return ADMIN
                "ROLE_MEMBER" -> return MEMBER
            }

            return MEMBER
        }
    }
}