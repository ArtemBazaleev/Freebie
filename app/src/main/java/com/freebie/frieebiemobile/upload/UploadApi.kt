package com.freebie.frieebiemobile.upload

import com.freebie.frieebiemobile.network.HttpAccess
import com.freebie.frieebiemobile.network.Method
import com.freebie.protos.CommonModelProtos.PartialUploadCreateRequest
import com.freebie.protos.CommonModelProtos.PartialUploadCreateResponse
import javax.inject.Inject

interface UploadApi {
}

class UploadApiImpl @Inject constructor(
    private val httpAccess: HttpAccess
) {
    suspend fun createUpload(
        contentType: String, //image/jpeg
        fileName: String,
        fileLength: Long,
        filePartsCount: Int
    ) = runCatching {
        val request = PartialUploadCreateRequest
            .newBuilder()
            .setContentType(contentType)
            .setFileName(fileName)
            .setLength(fileLength)
            .setParts(filePartsCount) //part - int значение от 1 и до того сколько частей пришлешь мне.
            .setType("company")
            .build()

        val response = httpAccess.httpRequest(
            requestUrlSegment = CREATE_UPLOAD,
            method = Method.POST,
            body = request.toByteArray()
        )
        PartialUploadCreateResponse.parseFrom(response.bodyAsArray)
    }

//    suspend fun uploadChunk() = runCatching {
//        val request = PartialUpload
//    }

    companion object {
        const val CREATE_UPLOAD = "v1/partial/upload"
        const val PARTIAL_UPLOAD = "v1/partial/upload/"
    }
}