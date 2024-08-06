package kr.dohoonkim.blog.restapi.units.application.file

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.ints.beGreaterThan
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.file.FileUploadService
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnsupportedMediaTypeException
import kr.dohoonkim.blog.restapi.common.utility.ImageResizer
import kr.dohoonkim.blog.restapi.common.utility.ImageTypeValidator
import kr.dohoonkim.blog.restapi.support.file.createImageMockMultipartFile
import kr.dohoonkim.blog.restapi.support.file.createTextMockMultipartFile
import org.springframework.mock.web.MockMultipartFile
import java.io.File
import java.nio.file.Files

internal class FileUploadServiceTest: BehaviorSpec({
    val imageTypeValidator = ImageTypeValidator()
    val imageResizer = ImageResizer()
    val fileSystem = Jimfs.newFileSystem(Configuration.osX())
    val basePath = fileSystem.getPath("/virtual")
    val fileService = FileUploadService(
        imageTypeValidator=imageTypeValidator,
        imageResizer=imageResizer,
        host="localhost",
        basePath = basePath
    )

    beforeSpec {
        Files.createDirectories(basePath)
    }

    Given("이미지 파일이 주어진다") {
        val multipart = createImageMockMultipartFile("test", "jpeg")

        When("업로드 요청을 하면") {
            val result = fileService.createImage(multipart)
            Then("업로드 된다") {
                result.length shouldBeGreaterThan 0
            }
        }
    }

    Given("지원하지 않는 파일이 주어진다") {
        val txtFile = createTextMockMultipartFile("test", "hello world")
        When("업로드 요청을 하면") {
            Then("UnSupportedMediaTypeException이 발생한다") {
                shouldThrow<UnsupportedMediaTypeException> {
                    fileService.createImage(txtFile)
                }.message shouldBe ErrorCodes.NOT_SUPPORT_IMAGE_TYPE.message
            }
        }
    }

    afterEach {
    }
})