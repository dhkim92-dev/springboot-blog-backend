package kr.dohoonkim.blog.restapi.common.response

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

abstract class BaseResponse(
    @get:JsonProperty("_links")
    @get:Schema(
        name = "_links",
        description = "HATEOAS 링크",
        example = """
        {
          "self": {
            "href": "https://api.example.com/resource"
          }
        }
        """
    )
    val _links: MutableMap<String, HateoasLink> = mutableMapOf()
) {}
