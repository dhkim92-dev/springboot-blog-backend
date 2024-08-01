package kr.dohoonkim.blog.restapi.units.interfaces

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.application.member.MemberService
import kr.dohoonkim.blog.restapi.application.member.dto.*
import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.exceptions.ConflictException
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.interfaces.member.MemberController
import kr.dohoonkim.blog.restapi.support.entity.createMember

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

        val response = memberController.createMember(request)
        response shouldBe data
    }

    @Test
    fun `존재하지 않는 이메일 주소 대해 중복 여부를 확인한다`() {
        every { memberService.checkEmailAvailable(any()) } returns true

        shouldNotThrowAny {
            val response = memberController.checkEmail("exists@dohoon-kim.kr")
            response shouldBe true
        }
    }

    @Test
    fun `존재하는 이메일 주소에 대해 중복 여부를 확인한다`() {
        every { memberService.checkEmailAvailable(any()) } throws ConflictException(ErrorCode.ALREADY_EXIST_EMAIL)

        shouldThrow<ConflictException> {
            memberController.checkEmail("duplicated@dohoon-kim.kr")
        }
    }

    @Test
    fun `존재하지 않는 닉네임 중복 여부를 확인한다`() {
        every { memberService.checkNicknameAvailable(any()) } returns true

        shouldNotThrowAny {
            val response = memberController.checkNickname("available")
            response shouldBe true
        }
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
        every { memberService.changeMemberNickname(user.id, any()) } returns data

        val response = memberController.changeNickname(user.id, request, user.id)
        response shouldBe data
    }

    @Test
    fun `이메일을 변경한다`() {
        val request = EmailChangeRequest(email = "newEmail@gmail.com")
        val data = MemberDto(id = user.id,
            email = "newEmail@gmail.com",
            nickname = user.nickname,
            role = user.role.name,
            isActivated = true)

        every { memberService.changeMemberEmail(user.id, any()) } returns data

        val response = memberController.changeEmail(user.id, request, user.id)
        response shouldBe data
    }

    @Test
    fun `비밀번호를 변경한다`() {
        val request = PasswordChangeRequest(currentPassword = user.password, newPassword = "newPassword1234")
        user.updatePassword(request.newPassword)
        val data = MemberDto.fromEntity(user)

        every { memberService.changePassword(user.id, any()) } returns data

        val response = memberController.changePassword(user.id, request, user.id)
        response shouldBe data
    }

    @Test
    fun `회원탈퇴를 한다`() {
        every { memberService.delete(user.id, user.id) } returns Unit

        val response = memberController.withdrawal(user.id, user.id)

        response shouldBe Unit
    }

    @AfterEach
    fun `clearMocks`() {
        clearAllMocks()
    }
}