package kr.dohoonkim.blog.restapi.common.error

import org.springframework.http.HttpStatus

enum class ErrorCode(val status : HttpStatus, val code : String, val message : String) {

    // GLOBAl ERRORS
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G001", "내부 서버 오류입니다."),
    AUTHENTICATION_FAIL(HttpStatus.UNAUTHORIZED, "G002", "인증에 실패하였습니다."),
    NO_PERMISSION(HttpStatus.FORBIDDEN, "G003", "권한이 없습니다."),
    NOT_SUPPORT_IMAGE_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "G004", "지원하지 않는 이미지 형식입니다."),
    NOT_SUPPORT_VIDEO_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "G005", "지원하지 않는 비디오 형식입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "G006", "잘못된 입력값입니다."),
    INVALID_HEADER(HttpStatus.BAD_REQUEST, "G007", "잘못된 헤더값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "G008", "지원하지 않는 메소드입니다."),
    HTTP_MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST, "G009", "request body가 없거나, 값이 올바르지 않습니다."),
    RESOURCE_OWNERSHIP_VIOLATION(HttpStatus.FORBIDDEN, "G010", "접근 권한이 없는 리소스입니다."),
    NOT_IMPLEMENTED(HttpStatus.NOT_IMPLEMENTED, "G011", "아직 구현되지 않은 서비스입니다."),

    // MEMBER EXCEPTION
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "존재하지 않는 사용자입니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.CONFLICT, "M002", "이미 가입된 이메일입니다."),
    ALREADY_EXIST_NICKNAME(HttpStatus.CONFLICT, "M003", "이미 존재하는 닉네임입니다."),
    UPDATE_PASSWORD_FAILED(HttpStatus.UNAUTHORIZED, "M004", "현재 패스워드가 일치하지 않습니다."),

    // ARTICLE
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "존재하지 않는 게시물입니다."),

    // COMMENT
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "존재하지 않는 댓글입니다."),

    // ARTICLE_CATEGORY
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "AC01", "존재하지 않는 게시물 카테고리입니다."),
    ALREADY_EXIST_CATEGORY(HttpStatus.CONFLICT, "AC02", "이미 존재하는 카테고리입니다."),


    //AUTHENTICATION
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "A001", "로그인에 실패했습니다."),
    INVALID_PAYLOAD_EXCEPTION(HttpStatus.UNAUTHORIZED, "J001", "JWT 페이로드가 잘못되었습니다."),
    EXPIRED_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "J002", "만료된 토큰입니다."),
    INVALID_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "J003", "인증 헤더 정보가 잘못되었습니다."),
    NOT_VERIFIED_EMAIL(HttpStatus.UNAUTHORIZED, "A002", "이메일 인증이 완료되지 않은 계정입니다."),
    NOT_SUPPORTED_OAUTH2_PROVIDER(HttpStatus.BAD_REQUEST, "A003", "지원하지 않는 OAuth2 Provider 입니다."),
    OAUTH2_NOT_VERIFIED_EMAIL(HttpStatus.UNAUTHORIZED, "A004", "이메일 인증이 완료되지 않은 소셜 계정입니다.")
}