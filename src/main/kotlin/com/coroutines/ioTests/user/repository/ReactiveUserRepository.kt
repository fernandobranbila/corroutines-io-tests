package com.coroutines.ioTests.user.repository

import com.coroutines.ioTests.user.document.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface ReactiveUserRepository: ReactiveMongoRepository<User, Long> {

    fun findByName(name: String): Flow<User>
}