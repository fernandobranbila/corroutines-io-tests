package com.corroutines.ioTests.file.entrypoint

import com.corroutines.ioTests.file.service.FileServiceAsync
import com.corroutines.ioTests.file.service.FileServiceSync
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

    suspend fun processOneFileSync(request: ServerRequest): ServerResponse {
        val time = measureTimeMillis {
            fileServiceSync.processOneImage()
        }
        return ServerResponse.ok().json().bodyValue(time).awaitSingle()
    }

    suspend fun processManyFilesSync(request: ServerRequest): ServerResponse {
        val time = measureTimeMillis {
            fileServiceSync.processImages()
        }
        return ServerResponse.ok().json().bodyValue(time).awaitSingle()
    }

    suspend fun processOneFileAsync(request: ServerRequest): ServerResponse {
        val time = measureTimeMillis {
            fileServiceAsync.processOneImage()
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