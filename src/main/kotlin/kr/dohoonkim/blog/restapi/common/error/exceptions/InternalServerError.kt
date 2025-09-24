package kr.dohoonkim.blog.restapi.common.error.exceptions

import org.springframework.http.HttpStatus

class InternalServerError(
    message: String = "Internal Server Error"
): BusinessException(
    status = HttpStatus.INTERNAL_SERVER_ERROR,
    code = "internal-server-error",
    message = message
) {

}