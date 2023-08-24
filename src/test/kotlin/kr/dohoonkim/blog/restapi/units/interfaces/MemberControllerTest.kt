package kr.dohoonkim.blog.restapi.units.interfaces

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.application.member.MemberService
import kr.dohoonkim.blog.restapi.application.member.dto.*
import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.exceptions.ConflictException
import kr.dohoonkim.blog.restapi.common.response.ApiResult
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.common.response.ResultCode.*
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.interfaces.MemberController
import kr.dohoonkim.blog.restapi.support.entity.createMember
import org.springframework.http.HttpStatus.OK
import java.lang.IllegalArgumentException

class MemberControllerTest : AnnotationSpec() {

    private val memberService = mockk<MemberService>()
    private val memberController = MemberController(memberService)
    private lateinit var user : Member

    @BeforeEach
    fun setUp() {
        user = createMember()
    }

    @Test
    fun `사용자가 회원가입 폼을 제출한다`() {
        val request = MemberJoinRequest(nickname = user.nickname, email = user.email, password = user.password)
        val data = MemberDto.fromEntity(user)

        every { memberService.create(any()) } returns data

        val response = memberController.createMember(request).body!!
        response.status shouldBe CREATE_MEMBER_SUCCESS.status.value()
        response.code shouldBe CREATE_MEMBER_SUCCESS.code
        response.data shouldBe data
    }

    @Test
    fun `존재하지 않는 이메일 주소 대해 중복 여부를 확인한다`() {
        every { memberService.checkEmailAvailable(any()) } returns true

        val response = memberController.checkEmail("exists@dohoon-kim.kr").body!!
        response.status shouldBe AVAILABLE_EMAIL.status.value()
        response.code shouldBe AVAILABLE_EMAIL.code
    }

    @Test
    fun `존재하는 이메일 주소에 대해 중복 여부를 확인한다`() {
        every { memberService.checkEmailAvailable(any()) } throws ConflictException(ErrorCode.ALREADY_EXIST_EMAIL)

        shouldThrow<ConflictException> {
            val response = memberController.checkEmail("duplicated@dohoon-kim.kr")
            println(response)
        }
    }

    @Test
    fun `존재하지 않는 닉네임 중복 여부를 확인한다`() {
        every { memberService.checkNicknameAvailable(any()) } returns true

        val response = memberController.checkNickname("available").body!!
        response.status shouldBe AVAILABLE_NICKNAME.status.value()
        response.code shouldBe AVAILABLE_NICKNAME.code
    }

    @Test
    fun `존재하는 닉네임에 대해 닉네임 존재 여부를 확인한다`() {
        every { memberService.checkNicknameAvailable(any()) } throws ConflictException(ErrorCode.ALREADY_EXIST_NICKNAME)

        shouldThrow<ConflictException> {
            memberController.checkNickname("duplicated")
        }
    }

    @Test
    fun `닉네임을 변경한다`() {
        val request = NicknameChangeRequest(nickname = "new_nickname")
        val data = MemberDto(
            id = user.id,
            nickname = "new_nickname",
            email = user.email,
            role = user.role.rolename,
            isActivated = true
        )
        every { memberService.changeMemberNickname(any()) } returns data

        val response = memberController.changeNickname(user.id, request).body!!

        response.status shouldBe CHANGE_NICKNAME_SUCCESS.status.value()
        response.code shouldBe CHANGE_NICKNAME_SUCCESS.code
        response.data shouldBe data
    }

    @Test
    fun `이메일을 변경한다`() {
        val request = EmailChangeRequest(email = "newEmail@gmail.com")
        val data = MemberDto(id = user.id,
            email = "newEmail@gmail.com",
            nickname = user.nickname,
            role = user.role.name,
            isActivated = true)

        every { memberService.changeMemberEmail(any()) } returns data

        val response = memberController.changeEmail(user.id, request).body!!

        response.status shouldBe CHANGE_EMAIL_SUCCESS.status.value()
        response.code shouldBe CHANGE_EMAIL_SUCCESS.code
        response.data shouldBe data
    }

    @Test
    fun `비밀번호를 변경한다`() {
        val request = PasswordChangeRequest(currentPassword = user.password, newPassword = "newPassword1234")
        user.updatePassword(request.newPassword)
        val data = MemberDto.fromEntity(user)

        every { memberService.changePassword(any()) } returns data

        val response = memberController.changePassword(user.id, request).body!!
        response.status shouldBe CHANGE_PASSWORD_SUCCESS.status.value()
        response.code shouldBe CHANGE_PASSWORD_SUCCESS.code
        response.data shouldBe data
    }

    @Test
    fun `회원탈퇴를 한다`() {
        every { memberService.delete(user.id) } returns Unit

        val response = memberController.withdrawal(user.id).body!!

        response.status shouldBe DELETE_MEMBER_SUCCESS.status.value()
        response.data shouldBe Unit
    }

}