package com.coroutines.ioTests.user.entrypoint

import com.coroutines.ioTests.user.document.User
import com.coroutines.ioTests.user.repository.ReactiveUserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class UserHandler(
        private val reactiveUserRepository: ReactiveUserRepository,
) {

    suspend fun saveConcurrent(request: ServerRequest): ServerResponse =
            coroutineScope {
                val body = request.awaitBody<UserRequest>()
                val ab = body.users.map { user ->
                    withContext(Dispatchers.IO) {
                        async {
                            reactiveUserRepository.save(user).awaitFirst()
                        }
                    }
                }
                return@coroutineScope ServerResponse.ok().json().bodyValue(ab.awaitAll()).awaitSingle()
            }


    suspend fun getByNameConcurrent(request: ServerRequest): ServerResponse {
        val name = request.queryParam("name").get()
        val users = reactiveUserRepository.findByName(name)
        return ServerResponse.ok().json().bodyAndAwait(users)
    }

    suspend fun getAllConcurrent(request: ServerRequest): ServerResponse {
        val users = reactiveUserRepository.findAll().asFlow()
        return ServerResponse.ok().json().bodyAndAwait(users)
    }

}