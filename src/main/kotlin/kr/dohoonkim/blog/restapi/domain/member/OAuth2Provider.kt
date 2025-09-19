package kr.dohoonkim.blog.restapi.domain.member


enum class OAuth2Provider(val providerName: String){

    GOOGLE("google"),
    GITHUB("github")
}