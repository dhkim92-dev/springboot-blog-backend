package kr.dohoonkim.blog.restapi.common.response

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.domain.Pageable

class PageList<T>(
        val total : Long,
        val count : Int,
        @JsonProperty("page_size")
        val pageSize : Int,
        @JsonProperty("page_num")
        val pageNum : Int,
        val data : List<T>,
){
    companion object {
        fun <T> of(data : List<T>, total : Long, pageable : Pageable) : PageList<T> {
            return  PageList(total, data.size, pageable.pageSize, pageable.pageNumber, data);
        }
    }
}