package com.coroutines.ioTests.file.entrypoint

import com.coroutines.ioTests.file.service.FileServiceAsync
import com.coroutines.ioTests.file.service.FileServiceSync
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.json
import kotlin.system.measureTimeMillis

@Component
class FilesHandler(
        private val fileServiceSync: FileServiceSync,
        private val fileServiceAsync: FileServiceAsync,
) {

    suspend fun processManyFilesSync(request: ServerRequest): ServerResponse {
        val time = measureTimeMillis {
            fileServiceSync.processImages()
        }
        return ServerResponse.ok().json().bodyValue(time).awaitSingle()
    }


    suspend fun processManyFilesAsync(request: ServerRequest): ServerResponse {
        val time = measureTimeMillis {
            val test = fileServiceAsync.processImages()
        }
        return ServerResponse.ok().json().bodyValue(time).awaitSingle()
    }
}