package com.corroutines.ioTests.digimon.service

import com.corroutines.ioTests.digimon.model.Digimon
import kotlinx.coroutines.*
import org.springframework.core.ParameterizedTypeReference
import org.springframework.core.io.ResourceLoader
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange
import org.springframework.web.reactive.function.client.bodyToFlow
import java.time.Instant
import java.time.LocalDateTime
import kotlin.system.measureTimeMillis

@Component
class DigimonService(
        private val resourceLoader: ResourceLoader,
        private val restTemplate: RestTemplate,
) {

    //TODO(REFATORAR WEBCLIENT)
    val webClient = WebClient.builder().baseUrl("https://digimon-api.vercel.app/api/digimon/name").build()

    val FILE_PATH = "classpath:digimon.txt"

    suspend fun findAllDigimonsAsync(): Long {
        return coroutineScope {
            val digimons = getDigimonsFromFile()
            val time = measureTimeMillis {
                val digimonsAPIResponse = withContext(Dispatchers.Default) {
                    val requests = digimons.asSequence().map { digimon ->
                        async {
                            webClient.get().uri { uriBuilder ->
                                uriBuilder.path("/{name}")
                                        .build(digimon)
                            }.awaitExchange().awaitBody<List<Digimon>>()
                        }
                    }.toList()
                    requests.awaitAll()
                }
            }
            time
        }
    }

    fun findAllDigimonsSync(): Long {
        val startTime = Instant.now()
        val digimons = getDigimonsFromFile()
        val headers = HttpHeaders()
        val entity = HttpEntity<Any>(headers)
        val time = measureTimeMillis {
            val digimonsAPIResponse = digimons.asSequence().map { digimon ->
                restTemplate.exchange("https://digimon-api.vercel.app/api/digimon/name/$digimon", HttpMethod.GET, entity, object : ParameterizedTypeReference<List<Digimon>>() {}).body
            }.toList()
        }
        return time
    }

    private fun getDigimonsFromFile() =
            resourceLoader.getResource(FILE_PATH).file.readText(charset = Charsets.UTF_8).split("\n")

}