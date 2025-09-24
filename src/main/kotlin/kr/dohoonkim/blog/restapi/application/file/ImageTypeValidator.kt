package kr.dohoonkim.blog.restapi.application.file

import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnsupportedMediaTypeException
import org.springframework.stereotype.Component

@Component
class ImageTypeValidator() {

    fun validate(bytes: ByteArray) {
        if (isJpeg(bytes) || isPng(bytes) || isGif(bytes)) {
            return
        } else {
            throw UnsupportedMediaTypeException(ErrorCodes.NOT_SUPPORT_IMAGE_TYPE)
        }
    }

    private fun isJpeg(bytes: ByteArray): Boolean {
        return bytes.size >= 3 &&
                bytes[0] == 0xFF.toByte() &&
                bytes[1] == 0xD8.toByte() &&
                bytes[2] == 0xFF.toByte()
    }

    private fun isPng(bytes: ByteArray): Boolean {
        return bytes.size >= 8 &&
                bytes[0] == 0x89.toByte() &&
                bytes[1] == 0x50.toByte() && // P
                bytes[2] == 0x4E.toByte() && // N
                bytes[3] == 0x47.toByte() && // G
                bytes[4] == 0x0D.toByte() && // CR
                bytes[5] == 0x0A.toByte() && // LF
                bytes[6] == 0x1A.toByte() && // EOF
                bytes[7] == 0x0A.toByte()    // LF
    }

    private fun isGif(bytes: ByteArray): Boolean {
        return bytes.size >= 6 &&
                ((bytes[0] == 0x47.toByte() && // G
                  bytes[1] == 0x49.toByte() && // I
                  bytes[2] == 0x46.toByte() && // F
                  bytes[3] == 0x38.toByte() && // 8
                  (bytes[4] == 0x39.toByte() || bytes[4] == 0x37.toByte()) && // 9 or 7
                  bytes[5] == 0x61.toByte()) || // a
                 (bytes[0] == 0x47.toByte() && // G
                  bytes[1] == 0x49.toByte() && // I
                  bytes[2] == 0x46.toByte() && // F
                  bytes[3] == 0x38.toByte() && // 8
                  (bytes[4] == 0x39.toByte() || bytes[4] == 0x37.toByte()) && // 9 or 7
                  bytes[5] == 0x61.toByte()))   // a
    }
}