package kr.dohoonkim.blog.restapi.application.member

import kr.dohoonkim.blog.restapi.application.member.dto.*
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.ConflictException
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnauthorizedException
import kr.dohoonkim.blog.restapi.common.response.PageList
import kr.dohoonkim.blog.restapi.common.utility.AuthenticationUtil
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val authenticationUtil: AuthenticationUtil
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun create(dto: MemberCreateCommand): MemberDto {
        if (checkEmailExist(dto.email)) {
            throw ConflictException(ErrorCodes.ALREADY_EXIST_EMAIL)
        }

        if (checkNicknameExist(dto.nickname)) {
            throw ConflictException(ErrorCodes.ALREADY_EXIST_NICKNAME)
        }

        val encodedPassword = passwordEncoder.encode(dto.password);
        var member: Member = Member(dto.nickname, dto.email, encodedPassword, null);
        return MemberDto.fromEntity(memberRepository.save(member));
    }

    @Transactional
    fun delete(memberId: UUID, resourceId : UUID) {
        authenticationUtil.checkPermission(memberId, resourceId)
        memberRepository.deleteById(memberId);
    }

//    @Transactional
//    fun getMemberByEmail(email: String): MemberDto {
//        return MemberDto.fromEntity(
//            memberRepository.findByEmail(email)
//                ?: throw NotFoundException(ErrorCodes.MEMBER_NOT_FOUND)
//        );
//    }

    @Transactional
    fun getMembers(pageable: Pageable?): PageList<MemberDto> {
        if (!authenticationUtil.isAdmin()) throw ForbiddenException(ErrorCodes.RESOURCE_OWNERSHIP_VIOLATION)
        val total = memberRepository.count();
        val page = memberRepository.findAll()
        val data: List<MemberDto> = page.toList().map { member -> MemberDto.fromEntity(member) }

        return PageList.of(data, total, pageable ?: Pageable.ofSize(20))
    }

    @Transactional
    fun checkEmailAvailable(email: String): Boolean {
        if (checkEmailExist(email)) {
            throw ConflictException(ErrorCodes.ALREADY_EXIST_EMAIL)
        }

        return true;
    }

    @Transactional
    fun checkNicknameAvailable(nickname: String): Boolean {
        if (checkNicknameExist(nickname)) {
            throw ConflictException(ErrorCodes.ALREADY_EXIST_NICKNAME)
        }

        return true;
    }

    @Transactional
    fun changeMemberNickname(memberId: UUID, dto: NicknameChangeCommand): MemberDto {
        if (checkNicknameExist(dto.nickname)) {
            throw ConflictException(ErrorCodes.ALREADY_EXIST_NICKNAME)
        }

        val target = memberRepository.findByMemberId(dto.memberId)

        authenticationUtil.checkPermission(memberId, target.id)
        target.updateNickname(dto.nickname)

        return MemberDto.fromEntity(memberRepository.save(target))
    }

    @Transactional
    fun changeMemberEmail(memberId: UUID, dto: EmailChangeCommand): MemberDto {
        if (checkEmailExist(dto.email)) {
            throw ConflictException(ErrorCodes.ALREADY_EXIST_EMAIL)
        }
        val target = memberRepository.findByMemberId(dto.memberId)
        authenticationUtil.checkPermission(memberId, target.id)
        target.updateEmail(dto.email)

        return MemberDto.fromEntity(memberRepository.save(target))
    }

    @Transactional
    fun changePassword(memberId: UUID, dto: PasswordChangeCommand): MemberDto {
        val target = memberRepository.findByMemberId(dto.memberId)

        authenticationUtil.checkPermission(memberId, target.id)

        if(!authenticationUtil.isAdmin() && !passwordEncoder.matches(dto.currentPassword, target.password)) {
            throw UnauthorizedException(ErrorCodes.UPDATE_PASSWORD_FAILED)
        }

        target.updatePassword(passwordEncoder.encode(dto.newPassword))

        return MemberDto.fromEntity(memberRepository.save(target))
    }

    private fun checkEmailExist(email: String): Boolean {
        return memberRepository.existsByEmail(email);
    }

    private fun checkNicknameExist(nickname: String): Boolean {
        return memberRepository.existsByNickname(nickname);
    }

}