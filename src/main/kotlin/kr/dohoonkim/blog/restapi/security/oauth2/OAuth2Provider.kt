package kr.dohoonkim.blog.restapi.security.oauth2

enum class OAuth2Provider(val providerName: String){

    GOOGLE("google"),
    GITHUB("github")
}