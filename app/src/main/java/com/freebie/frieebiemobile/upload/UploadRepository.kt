package com.freebie.frieebiemobile.upload

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.math.ceil

interface UploadRepository {
}

class UploadRepositoryImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val api: UploadApiImpl
) : UploadRepository, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob()

    private fun uploadFile(file: File): Flow<String> {
        val resultFlow = MutableSharedFlow<String>()
        launch {
            val compressedFile = Compressor.compress(
                context = appContext,
                imageFile = file,
                coroutineContext = coroutineContext
            ) {
                quality(70)
                format(Bitmap.CompressFormat.JPEG)
            }
            val result = partialFileUpload(compressedFile)
            resultFlow.emit(result)
        }
        return resultFlow
    }


    suspend fun partialFileUpload(
        fileToUpload: File
    ): String = withContext(Dispatchers.IO) {
        val iterationCount = ceil((fileToUpload.length().toFloat() / MAX_UPLOAD_CHUNK_SIZE)).toInt()

        Log.d("partialFileUpload", "partialFileUpload file = ${fileToUpload.absolutePath} iterations = $iterationCount")
        val response = api.createUpload(
            contentType = IMAGE_NAME,
            fileName = fileToUpload.name,
            fileLength = fileToUpload.length(),
            filePartsCount = iterationCount
        )
        response.onSuccess { uploadResponse ->
            Log.d("partialFileUpload", "upload created id = ${uploadResponse.uploadId}")
            fileToUpload.inputStream().use { inputStream ->
                val buffer = ByteArray(MAX_UPLOAD_CHUNK_SIZE)
                repeat(iterationCount) { chunkCount ->
                    inputStream.read(buffer, 0, MAX_UPLOAD_CHUNK_SIZE)

                    Log.d("partialFileUpload", "start to upload chunk $chunkCount buffer = $buffer")
                    val uploadedChunk = api.uploadChunk(uploadResponse.uploadId, buffer, chunkCount + 1)
                    Log.d("partialFileUpload", "uploadedChunk count = $chunkCount id = ${uploadedChunk.getOrNull()?.uploadId}")
                }
            }
        }.onFailure {
            return@withContext ""
        }

        return@withContext ""
    }

    private fun RequestBody.toFormData(name: String, fileName: String?) =
        MultipartBody.Part.createFormData(name, fileName, this)

    companion object {
        private const val MAX_UPLOAD_CHUNK_SIZE = 256000
        private const val IMAGE_NAME = "image/*"
        private const val UPLOAD_FILE_FORM_DATA_NAME = "file"
    }
}