package com.corroutines.ioTests.digimon.entrypoint

import com.corroutines.ioTests.digimon.service.DigimonService
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class DigimonHandler(
        private val digimonService: DigimonService,
) {

    suspend fun getAllDigimonsAsync(request: ServerRequest): ServerResponse {
        val time = digimonService.findAllDigimonsAsync()
        return ServerResponse.ok().json().bodyValue(time).awaitSingle()
    }

    suspend fun getAllDigimonsSync(request: ServerRequest): ServerResponse {
        val time = digimonService.findAllDigimonsSync()
        return ServerResponse.ok().json().bodyValue(time).awaitSingle()
    }


}