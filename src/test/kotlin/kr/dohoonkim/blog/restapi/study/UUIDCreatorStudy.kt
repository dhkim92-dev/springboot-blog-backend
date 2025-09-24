package kr.dohoonkim.blog.restapi.study

import com.github.f4b6a3.uuid.UuidCreator
import io.kotest.core.spec.style.AnnotationSpec
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID

class UUIDCreatorStudy : AnnotationSpec() {

    class Record(
        var id: UUID,
        var createdAt: LocalDateTime
    ) {

        var newId: UUID? = null

        fun makeNewId() {
            val inst = createdAt.atZone(java.time.ZoneId.systemDefault()).toInstant()
            newId = UuidCreator.getTimeOrderedEpoch(inst)
        }
    }

    @Test
    fun test() {
        // Given
        val uuid = UuidCreator.getTimeOrderedEpoch()
        println(uuid)
    }

    @Test
    fun migration() {
        val records = listOf(
            "304b3437-bcba-4e42-918c-e0a3cfbbb080" , "2025-08-01 14:37:38.273999",
            "8fb33f3f-43c4-404b-abbc-f601e5c9f3b7" ,  "2025-03-04 04:45:54.841341",
            "6c645bc7-2669-42fb-8953-fda38c2db83e" ,  "2025-01-24 16:58:13.882033",
            "cbbaa87d-8904-4e3f-958a-a1cf522e6cd6" ,  "2024-12-26 18:42:14.273657",
            "22cf40d6-54bc-4396-a246-cbb99de7c2d0" ,  "2024-12-25 22:44:28.601209",
            "ee59d813-d426-495e-9a36-0c3c7fdfcd8f" ,  "2024-12-22 18:48:23.108049",
            "1c974711-60b5-48ca-93bf-8fa08ff7db1a" ,  "2024-12-11 22:47:27.517417",
            "9c101134-70c9-44ba-9371-df3a2c96ddc9" ,  "2024-12-03 08:13:05.999501",
            "041fd3fa-6206-469d-8e6f-c46ae704232e" ,  "2024-12-03 04:51:54.880534",
            "3f2e3034-e84c-4d7a-b3f7-1c436d32b386" ,  "2024-11-20 23:14:44.902087",
            "eac6baf4-8f40-4bfc-9ddf-5e34224578d4" ,  "2024-11-20 09:55:06.868251",
            "59f944a2-72f0-4144-87cd-da7baad1b78d" ,  "2024-10-19 03:43:41.08414",
            "28fa5a26-c30f-4cde-b828-1e128a4b8af9" ,  "2024-08-15 00:04:57.027612",
            "efd12e72-8be4-4308-80d5-870de9300117" ,  "2024-07-19 18:52:32.72971",
            "c97ba390-0869-4fae-b2ad-b4fe5853ef36" ,  "2024-07-14 16:49:10.809559",
            "69adef46-cc46-4f6e-9b7b-21aa370cd66b" ,  "2024-07-14 09:45:46.420057",
            "cf745bd1-581e-466b-8e82-5b4de5233298" ,  "2024-07-13 06:52:44.454476",
            "65054f81-9239-4ff2-9bad-7f457f4b4a25" ,  "2024-07-12 14:43:53.254031",
            "8e22eac9-f745-4946-9b7f-5860a8c227e6" ,  "2024-07-12 08:09:19.566067",
            "bd4a452d-a83b-4113-ad61-2ce076d977b7" ,  "2024-07-10 13:18:26.680721",
            "49fe2f0d-fd3a-4f0c-bcbf-4ed5b0be8dbd" ,  "2024-07-02 05:51:57.394161",
            "6691d440-68ef-4bf7-b53e-548e7394b05e" ,  "2024-06-28 05:26:40.06949",
            "2b08d269-cd0e-4283-978d-a51eff21c5cd" ,  "2024-06-19 21:58:59.20464",
            "f31e7af9-f15d-440d-bdb4-77a6275fabef" ,  "2024-06-18 17:05:50.285055",
            "f16fab05-5695-4d08-ba67-6e0ecc489179" ,  "2024-03-22 06:27:10.959752",
            "6c450311-954a-4057-b845-812f13ad3e68" ,  "2023-11-13 04:01:30.765399",
            "cc832d2f-65f4-4729-b564-58db540cdde1" ,  "2023-11-03 11:11:16.724819",
            "ffbae8aa-4558-4502-987e-1d1e8bd4b6c8" ,  "2023-11-03 07:21:25.723692",
            "dc3017e9-23b2-458e-9d4b-10a0cb1c1843" ,  "2023-11-02 05:07:53.379047",
            "8bf1a2b8-c1f1-479a-88cb-a277dc842c9e" ,  "2023-10-31 12:31:30.5341",
            "a6c1fb0e-47ee-45d2-bff2-7bca8ab28b5b" ,  "2023-10-28 13:00:57.438269",
            "c4864f4a-9c45-4663-809b-e9ba76711159" ,  "2023-10-18 17:09:31.301903",
            "bc1d946e-1c61-450a-8677-25209e795001" ,  "2023-10-16 10:38:17.074129",
            "88f4d598-7cce-4277-8e84-d60ec5346feb" ,  "2023-10-16 10:04:49.042151",
            "abef8bd4-eaa0-4c7e-9e05-3d6a645eb4ed" ,  "2023-09-04 06:46:48.381859",
            "e895d2ff-4a5d-40f5-93dd-d8b16ebb1c0a" ,  "2023-08-24 00:00:00",
            "47b1d416-0c8a-4b42-9e6c-f3e116f33706","2022-06-01 00:00:00",
            "47dd9946-5998-4790-afa7-36098453df12","2022-08-01 00:00:00",
            "0c5b3964-7864-4ce8-8d8d-42434e0deb14","2022-08-05 00:00:00",
            "d1940300-d946-4053-8dca-bb0a8567d364","2022-08-22 00:00:00",
            "13f7d129-90ea-4be4-b1ba-d7a0f52aa964","2022-09-20 00:00:00",
            "a5b5927a-d331-4f4b-9836-2403055e0142","2022-10-12 00:00:00",
            "428c3b80-fb82-41cd-a58a-aae75a050a75","2022-10-24 00:00:00",
            "59758bca-cdd8-4da6-9150-e5f527633b5c","2023-02-14 00:00:00",
            "b08b63ad-a72f-4bd6-a749-9637768096d3","2023-03-07 00:00:00",
            "f01667df-73d5-4dca-bf6d-8ab6a0adb47e","2022-07-14 00:00:00",
            "b9d6bb64-b9fb-4c1c-a664-17a1eb81fa83","2022-07-14 00:00:00",
            "ee96f2c0-94c8-49be-9c12-6299ba68707a","2023-03-08 00:00:00",
            "e7961979-017c-4b2f-94d3-fe6f229bb803","2023-04-03 00:00:00",
            "62aa25e8-c620-41d4-ae75-9604892c6407","2023-04-13 00:00:00",
            "a7af634e-9db5-4fd8-a921-f6b69fe9a7f5","2023-06-26 00:00:00",
            "b38526fb-7ee1-4dcd-b868-5e7bf9665202","2023-08-19 00:00:00",
            "d6a50b96-17c2-4b58-88b9-9ba6faf0276b","2023-08-21 00:00:00",
            "5e2122c6-9804-4e22-b467-0f19d0e0a59e","2023-08-22 00:00:00",
            "92a88456-5e45-4506-a817-e0d3f8090462","2023-08-23 00:00:00"
        )

        var recordList = mutableListOf<Record>()
        for(i in records.indices step 2) {
            val id = UUID.fromString(records[i])
            val createdAt = LocalDateTime.parse(records[i + 1].replace(" ", "T"))
            recordList.add(Record(id, createdAt))
        }

        val originalSortResult = recordList.sortedBy { it.createdAt }.reversed()
        val newIdSorted = recordList.map {
            it.makeNewId()
            it
        }.sortedBy { it.newId }.reversed()

        // 두 정렬 결과가 동일한지 확인
        for(i in originalSortResult.indices) {
            val original = originalSortResult[i]
            val newId = newIdSorted[i]
            println("original: ${original.id}, ${original.createdAt} / newId: ${newId.newId}, ${newId.createdAt}")
        }

        // 쿼리 생성 POSTGRES
        // UPDATE article SET id = ':newId' where id = ':oldId';
        for (i in originalSortResult.indices) {
            val original = originalSortResult[i]
            val newId = newIdSorted[i]
            println("UPDATE article SET id = '${newId.newId}' where id = '${original.id}';")
        }
        println("rollback")
        for (i in originalSortResult.indices) {
            val original = originalSortResult[i]
            val newId = newIdSorted[i]
            println("UPDATE article SET id = '${original.id}' where id = '${newId.newId}';")
        }
    }
}