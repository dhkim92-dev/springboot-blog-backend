package kr.dohoonkim.blog.restapi.application.board.usecases.article

import kr.dohoonkim.blog.restapi.application.board.dto.article.CreatePostCommand
import kr.dohoonkim.blog.restapi.domain.member.Member
import java.util.UUID

interface CreatePostUseCase {

    /**
     * 새로운 게시물을 생성한다.
     * @param loginId 게시물을 생성하는 회원의 UUID
     * @param command 게시물 생성에 필요한 정보
     * @return 생성된 게시물의 UUID
     */
    fun create(loginId: UUID, command: CreatePostCommand): UUID
}