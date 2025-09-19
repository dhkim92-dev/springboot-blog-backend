package kr.dohoonkim.blog.restapi.application.authentication.oauth2

enum class OAuth2Provider(val providerName: String){

    GOOGLE("google"),
    GITHUB("github")
}