package kr.dohoonkim.blog.restapi.common.error

import org.springframework.http.HttpStatus

interface ErrorCode {
    val code: String
    val message: String
    val status: HttpStatus
}