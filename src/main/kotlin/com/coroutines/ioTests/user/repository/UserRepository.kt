package com.coroutines.ioTests.user.repository

import com.coroutines.ioTests.user.document.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, Long> {

    fun findByName(name: String): List<User>
}