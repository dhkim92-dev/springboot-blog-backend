package kr.dohoonkim.blog.restapi.application.member

import kr.dohoonkim.blog.restapi.application.member.dto.*
import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.exceptions.ConflictException
import kr.dohoonkim.blog.restapi.common.error.exceptions.EntityNotFoundException
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
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
    private val memberRepository : MemberRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val authenticationUtil: AuthenticationUtil) {

    private val log : Logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun create(dto : MemberCreateDto) : MemberDto {
        if(this.checkEmailExist(dto.email)) {
            throw ConflictException(ErrorCode.ALREADY_EXIST_EMAIL)
        }

        if(this.checkNicknameExist(dto.nickname)) {
            throw ConflictException(ErrorCode.ALREADY_EXIST_NICKNAME)
        }

        val encodedPassword = this.passwordEncoder.encode(dto.password);
        log.info("password encoded : {}", encodedPassword)
        val member : Member = Member(dto.nickname, dto.email, encodedPassword, null);
        log.info("member {} try to join", member.toString())

        return MemberDto.fromEntity(this.memberRepository.save(member));
    }

    @Transactional
    fun delete(memberId : UUID) {
        if(!authenticationUtil.isAdmin() && !authenticationUtil.isResourceOwner(memberId)) {
            throw ForbiddenException(ErrorCode.RESOURCE_OWNERSHIP_VIOLATION)
        }

        memberRepository.deleteById(memberId);
    }

    @Transactional
    fun getMemberByEmail(email : String) : MemberDto {
        return MemberDto.fromEntity(this.memberRepository.findByEmail(email)
                ?: throw EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

//    @Transactional
//    fun getMemberById(memberId : UUID) : MemberDto {
//        return MemberDto.fromEntity(this.memberRepository.findByMemberId(memberId)
//                ?: throw NotFoundException())
//    }

    @Transactional
    fun getMembers(pageable : Pageable?) : PageList<MemberDto> {
        if(!authenticationUtil.isAdmin()) throw ForbiddenException(ErrorCode.RESOURCE_OWNERSHIP_VIOLATION)
        val total = memberRepository.count();
        val page = memberRepository.findAll()
        val data : List<MemberDto> = page.toList().map{ member-> MemberDto.fromEntity(member)}

        return PageList.of(data, total, pageable?:Pageable.ofSize( 20))
    }

    @Transactional
    fun checkEmailAvailable(email : String) : Boolean {
        log.info("email check request ${email}")
        if(this.checkEmailExist(email)) {
            throw ConflictException(ErrorCode.ALREADY_EXIST_EMAIL)
        }
        return true;
    }

    @Transactional
    fun checkNicknameAvailable(nickname : String) : Boolean {
        if(this.checkNicknameExist(nickname)) {
            throw ConflictException(ErrorCode.ALREADY_EXIST_NICKNAME)
        }
        return true;
    }

    @Transactional
    fun changeMemberNickname(dto : MemberNicknameChangeDto) : MemberDto {
        if(!authenticationUtil.isAdmin() && !authenticationUtil.isResourceOwner(dto.memberId)){
            throw ForbiddenException(ErrorCode.RESOURCE_OWNERSHIP_VIOLATION)
        }

        if(checkNicknameExist(dto.nickname)) {
            throw ConflictException(ErrorCode.ALREADY_EXIST_NICKNAME)
        }

        val target = memberRepository.findByMemberId(dto.memberId)
            ?: throw EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND)

        target.updateNickname(dto.nickname)

        return MemberDto.fromEntity(memberRepository.save(target))
    }

    @Transactional
    fun changeMemberEmail(dto : MemberEmailChangeDto) : MemberDto {
        if(!authenticationUtil.isAdmin() && !authenticationUtil.isResourceOwner(dto.memberId)){
            throw ForbiddenException(ErrorCode.RESOURCE_OWNERSHIP_VIOLATION)
        }

        if(checkEmailExist(dto.email)) {
            throw ConflictException(ErrorCode.ALREADY_EXIST_EMAIL)
        }

        val target = memberRepository.findByMemberId(dto.memberId)
            ?: throw EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND)

        target.updateEmail(dto.email)

        return MemberDto.fromEntity(memberRepository.save(target))
    }

    @Transactional
    fun changePassword(dto : MemberPasswordChangeDto) : MemberDto {
        if(!authenticationUtil.isAdmin() && !authenticationUtil.isResourceOwner(dto.memberId)) {
            throw ForbiddenException(ErrorCode.RESOURCE_OWNERSHIP_VIOLATION)
        }

        val target = memberRepository.findByMemberId(dto.memberId)
            ?: throw EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND)

        if(!passwordEncoder.matches(dto.currentPassword, target.password)) {
            throw UnauthorizedException(ErrorCode.UPDATE_PASSWORD_FAILED)
        }

        target.updatePassword(passwordEncoder.encode(dto.newPassword))

        return MemberDto.fromEntity(memberRepository.save(target))
    }

    private fun checkEmailExist(email : String) : Boolean {
        return this.memberRepository.existsByEmail(email);
    }

    private fun checkNicknameExist(nickname : String) : Boolean {
        return this.memberRepository.existsByNickname(nickname);
    }

}