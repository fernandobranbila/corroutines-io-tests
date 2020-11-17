package com.corroutines.ioTests.file.entrypoint

import com.corroutines.ioTests.file.service.FileService
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.json
import kotlin.system.measureTimeMillis

@Component
class FilesHandler(
        private val fileService: FileService,
) {

    suspend fun processOneFile(request: ServerRequest): ServerResponse {
        val time = measureTimeMillis {
            fileService.processOneImage()
        }
        return ServerResponse.ok().json().bodyValue(time).awaitSingle()
    }

    suspend fun processManyFiles(request: ServerRequest): ServerResponse {
        val time = measureTimeMillis {
            fileService.processImages()
        }
        return ServerResponse.ok().json().bodyValue(time).awaitSingle()
    }
}