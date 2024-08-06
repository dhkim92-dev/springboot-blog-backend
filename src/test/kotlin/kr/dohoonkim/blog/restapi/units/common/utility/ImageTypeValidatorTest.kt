package kr.dohoonkim.blog.restapi.units.common.utility

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import kr.dohoonkim.blog.restapi.common.utility.ImageTypeValidator
import kr.dohoonkim.blog.restapi.support.file.createImageMockMultipartFile
import org.springframework.mock.web.MockMultipartFile

internal class ImageTypeValidatorTest: AnnotationSpec() {

    private val imageTypeValidator = ImageTypeValidator()

    @BeforeEach
    fun setUp() {

    }

    @Test
    fun `jpeg가 제출되면 지원한다`() {
         val file = createImageMockMultipartFile("file","jpeg")

         imageTypeValidator.isSupportFile(file) shouldBe true
    }

    @Test
    fun `jpg가 제출되면 지원한다`() {
        val file = createImageMockMultipartFile("file","jpg")

        imageTypeValidator.isSupportFile(file) shouldBe true
    }

    @Test
    fun `png가 제출되면 지원한다`() {
        val file = createImageMockMultipartFile("file","png")

        imageTypeValidator.isSupportFile(file) shouldBe true
    }

    @Test
    fun `bmp가 제출되면 지원하지 않는다`() {
        val file = createImageMockMultipartFile("file","bmp")

        imageTypeValidator.isSupportFile(file) shouldBe false
    }


    @AfterEach
    fun clear() {
        clearAllMocks()
    }
}