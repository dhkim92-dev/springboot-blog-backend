package kr.dohoonkim.blog.restapi.application.authentication.usecases

import kr.dohoonkim.blog.restapi.application.authentication.dto.LoginResult

interface LoginByEmailPasswordUseCase {

    /**
     * 이메일 주소와 비밀번호로 로그인 시도
     * @param email 이메일 주소
     * @param password 비밀번호
     * @return 로그인 결과 (액세스 토큰, 리프레시 토큰)
     */
    fun login(email: String, password: String): LoginResult

}