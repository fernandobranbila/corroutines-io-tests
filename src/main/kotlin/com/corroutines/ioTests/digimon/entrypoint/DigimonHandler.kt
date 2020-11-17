package com.corroutines.ioTests.digimon.entrypoint

import com.corroutines.ioTests.digimon.service.DigimonService
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import kotlin.system.measureTimeMillis

@Component
class DigimonHandler(
        private val digimonService: DigimonService,
) {

    suspend fun getAllDigimonsAsync(request: ServerRequest): ServerResponse {
        val startTime = System.currentTimeMillis()
        val digimons = digimonService.findAllDigimonsAsync()
        val executionTime = System.currentTimeMillis() - startTime
        return ServerResponse.ok().json().bodyValue(DigimonResponse(executionTime, digimons)).awaitSingle()
    }

    suspend fun getAllDigimonsSync(request: ServerRequest): ServerResponse {
        val startTime = System.currentTimeMillis()
        val digimons = digimonService.findAllDigimonsSync()
        val executionTime = System.currentTimeMillis() - startTime
        return ServerResponse.ok().json().bodyValue(DigimonResponse(executionTime, digimons)).awaitSingle()
    }

}