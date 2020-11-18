package com.coroutines.ioTests.user.entrypoint

import com.coroutines.ioTests.user.document.User
import com.coroutines.ioTests.user.repository.ReactiveUserRepository
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class UserHandler(
        private val reactiveUserRepository: ReactiveUserRepository,
) {

    suspend fun saveConcurrent(request: ServerRequest): ServerResponse {
        val body = request.awaitBody<User>()
        val savedUser = reactiveUserRepository.save(body).awaitFirst()
        return ServerResponse.ok().json().bodyValue(savedUser).awaitSingle()
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