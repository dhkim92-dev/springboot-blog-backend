package kr.dohoonkim.blog.restapi.stash.interfaces.file

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import kr.dohoonkim.blog.restapi.application.file.FileUploadService
import kr.dohoonkim.blog.restapi.application.file.dto.ImageUploadResponse
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.common.response.annotation.ApplicationCode
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("api/")
@ApiResponses(
    value = [
        ApiResponse(responseCode = "401", description = "J002 - 만료된 토큰"),
        ApiResponse(responseCode = "403", description = "G003 - 권한 업음"),
        ApiResponse(responseCode = "409", description = "G004 - 지원하지 않는 이미지 타입")
    ]
)
class FileController(private val fileUploadService: FileUploadService) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "Image upload", description = "Upload Image file")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "FI01 - 이미지 업로드 성공"),
        ]
    )
    @PutMapping("v1/files/images",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ApplicationCode(ResultCode.IMAGE_UPLOAD_SUCCESS)
    fun uploadImage(@RequestBody(required = true) file: MultipartFile): ImageUploadResponse {
        return ImageUploadResponse(fileUploadService.createImage(file))
    }
}