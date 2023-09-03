package kr.dohoonkim.blog.restapi.common.response

import org.springframework.http.HttpStatus

import org.springframework.http.HttpStatus.*

enum class ResultCode(
    val status : HttpStatus,
    val code : String,
    val message : String
) {
    // GLOBAL
    HEALTH_CHECK_SUCCESS(OK, "G001", "서버 인스턴스 상태를 체크하였습니다."),
    // MEMBER
    CREATE_MEMBER_SUCCESS(CREATED, "M001", "회원 가입 완료했습니다."),
    GET_MEMBER_LIST_SUCCESS(OK, "M002", "회원 리스트를 조회하였습니다."),
    GET_MEMBER_INFO_SUCCESS(OK, "M003", "회원 정보를 조회하였습니다."),
    UPDATE_MEMBER_NICKNAME_SUCCESS(OK, "M004", "회원 닉네임을 변경하였습니다."),
    AVAILABLE_EMAIL(OK, "M005", "사용 가능한 이메일입니다."),
    AVAILABLE_NICKNAME(OK, "M006", "사용 가능한 닉네임입니다."),
    CHANGE_NICKNAME_SUCCESS(OK, "M007", "닉네임을 변경하였습니다."),
    CHANGE_PASSWORD_SUCCESS(OK, "M008", "비밀번호를 변경하였습니다."),
    CHANGE_EMAIL_SUCCESS(OK, "M009", "이메일을 변경하였습니다."),
    DELETE_MEMBER_SUCCESS(NO_CONTENT, "M010", "회원 정보를 삭제하였습니다."),

    // ARTICLE
    CREATE_ARTICLE_SUCCESS(CREATED, "P001", "게시물을 생성하였습니다."),
    MODIFY_ARTICLE_SUCCESS(OK, "P002", "게시물을 수정하였습니다."),
    DELETE_ARTICLE_SUCCESS(NO_CONTENT, "P003", "게시물을 삭제하였습니다."),
    GET_ARTICLE_SUCCESS(OK, "P004", "게시물을 조회하였습니다."),
    GET_ARTICLE_LIST_SUCCESS(OK, "P005", "게시물 목록을 조회하였습니다."),

    // COMMENT
    CREATE_COMMENT_SUCCESS(CREATED, "R001", "댓글을 생성하였습니다."),
    MODIFY_COMMENT_SUCCESS(OK, "R002", "댓글을 수정하였습니다."),
    DELETE_COMMENT_SUCCESS(NO_CONTENT, "R003", "댓글을 삭제하였습니다."),

    // CATEGORY
    CREATE_CATEGORY_SUCCESS(CREATED, "AC01", "신규 게시물 카테고리를 생성하였습니다."),
    MODIFY_CATEGORY_NAME_SUCCESS(OK, "AC02", "카테고리 이름을 수정하였습니다."),
    DELETE_CATEGORY_SUCCESS(NO_CONTENT, "AC03", "카테고리를 삭제하였습니다."),
    GET_CATEGORIES_SUCCESS(OK, "AC04", "카테고리 목록을 조회하였습니다."),
    GET_CATEGORY_DETAIL_SUCCESS(OK, "AC05", "카테고리 세부 정보를 조회하였습니다."),

    // ARTICLE_LIKE
    CREATE_ARTICLE_LIKE_SUCCESS(CREATED, "AC01", "신규 게시물 좋아요를 생성하였습니다."),
    DELETE_ARTICLE_LIKE_SUCCESS(NO_CONTENT, "AC03", "게시물 좋아요를 삭제하였습니다."),

    // AUTHENTICATION
    AUTHENTICATION_SUCCESS(OK, "A001", "회원 인증에 성공하였습니다."),
    REISSUE_TOKEN_SUCCESS(OK, "A002", "토큰을 재발급하였습니다."),
    TEMPOLARY_LOGIN_SUCCESS(OK, "A003", "임시 코드로 로그인하였습니다."),

    // FILE UPLOAD
    IMAGE_UPLOAD_SUCCESS(OK, "FI01", "이미지를 업로드 했습니다."),
}