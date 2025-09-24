package kr.dohoonkim.blog.restapi.interfaces.file

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import kr.dohoonkim.blog.restapi.application.file.FileService
import kr.dohoonkim.blog.restapi.application.file.dto.UploadFileCommand
import kr.dohoonkim.blog.restapi.common.annotations.MemberId
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.common.response.annotation.ApplicationCode
import kr.dohoonkim.blog.restapi.interfaces.file.dto.UploadFileResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@RestController
@SecurityRequirement(name = "bearer-jwt")
class FileController(
    private val fileService: FileService
) {

    @PutMapping("/api/v1/files/images")
    @SecurityRequirement(name = "bearer-jwt")
    @ApplicationCode(ResultCode.IMAGE_UPLOAD_SUCCESS)
    fun uploadImage(
        @MemberId loginId: UUID,
        @RequestBody(required=true) file: MultipartFile
    ): UploadFileResponse {
        return UploadFileResponse(url = fileService.uploadImage(
            loginId,
            UploadFileCommand(file=file)
        ).url)
    }
}