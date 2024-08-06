package kr.dohoonkim.blog.restapi.units.interfaces.file

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.application.file.FileUploadService
import kr.dohoonkim.blog.restapi.interfaces.file.FileController
import kr.dohoonkim.blog.restapi.support.file.createImageMockMultipartFile

internal class FileControllerTest: BehaviorSpec({

    val fileService = mockk<FileUploadService>()
    val fileController = FileController(fileService)

    Given("업로드 할 이미지가 주어진다") {
        val multipart = createImageMockMultipartFile("test", "png")

        When("업로드에 성공하면") {
            every { fileService.createImage(multipart) } returns "url.jpeg"
            Then("접근 URL이 반환된다") {
                val result = fileController.uploadImage(multipart)
                result.url.length shouldBeGreaterThan 1
            }
        }
    }
})