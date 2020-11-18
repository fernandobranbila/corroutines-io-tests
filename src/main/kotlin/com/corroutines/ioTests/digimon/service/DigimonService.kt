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

@Component
class DigimonService(
        private val resourceLoader: ResourceLoader,
        private val restTemplate: RestTemplate,
) {

    //TODO(REFATORAR WEBCLIENT)
    val webClient = WebClient.builder().baseUrl("https://digimon-api.vercel.app/api/digimon/name").build()

    val FILE_PATH = "classpath:digimon.txt"

    suspend fun findAllDigimonsAsync(): List<List<Digimon>> =
            //Começa o contexto de uma corroutine usando configurações padrões
            coroutineScope () {
                //Pega a lista de digimons de forma concorrente
                val digimons = getDigimonsFromFileSuspend()
                //Atribui o resultado da linha 49, onde o Deferred foi resolvido (await)
                val digimonsAPIResponse =
                        //Inicia uma nova corroutine em um novo contexto diferente da original ( Dispatcher diferentes tem pools de threads diferentes otimizadas pra tal execução)
                        withContext(Dispatchers.IO) {
                            //Recebe o retorno da chamada com o WebClient (Versão async do RestTemplate do Spring)
                            val digimonsAPIRequest =
                                    //Inicia uma nova corroutine para cada um dos elementos na lista (cada bloco async retorna um Deferred, um objeto não resolvido)
                                    digimons.asSequence().map { digimon -> //Sequence para deixar o código mais performático
                                        async  {
                                            logThreadNameSuspended()
                                            webClient.get().uri { uriBuilder ->
                                                uriBuilder.path("/{name}")
                                                        .build(digimon)
                                            }.awaitExchange().awaitBody<List<Digimon>>() //Sintaxe do WebClient para aguardar o request e o body
                                        }
                                    }.toList()
                            //awaitAll para resolver a lista de Deferred, tornando-os em List<List<Digimon> Obs: pra cada digimon é retornado uma lista
                            return@withContext digimonsAPIRequest.awaitAll()
                        }
                //Retorna o valor para o "coroutineScope" ao qual está atribuído na própria função findAllDigimonsAsync()
                return@coroutineScope digimonsAPIResponse
            }


    fun findAllDigimonsSync(): List<List<Digimon>?> {
        val digimons = getDigimonsFromFile()
        val headers = HttpHeaders()
        val entity = HttpEntity<Any>(headers)
        return digimons.asSequence().map { digimon ->
            logThreadName()
            restTemplate.exchange("https://digimon-api.vercel.app/api/digimon/name/$digimon", HttpMethod.GET, entity, object : ParameterizedTypeReference<List<Digimon>>() {}).body
        }.toList()
    }

    private suspend fun getDigimonsFromFileSuspend() =
            resourceLoader.getResource(FILE_PATH).file.readText(charset = Charsets.UTF_8).split("\n")

    private fun getDigimonsFromFile() =
            resourceLoader.getResource(FILE_PATH).file.readText(charset = Charsets.UTF_8).split("\n")

    private suspend fun logThreadNameSuspended() = println("Current Thread: ${Thread.currentThread().name }")

    private fun logThreadName() = println("Current Thread: ${Thread.currentThread().name }")

}