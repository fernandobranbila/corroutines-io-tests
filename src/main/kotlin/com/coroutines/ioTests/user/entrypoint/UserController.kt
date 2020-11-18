package com.coroutines.ioTests.user.entrypoint

import com.coroutines.ioTests.user.document.User
import com.coroutines.ioTests.user.repository.UserRepository
import org.springframework.web.bind.annotation.*

//Created just to split concurrent from non-concurrent for benchmarking reasons
@RestController
@RequestMapping("/users-non-concurrent")
class UserController(
        private val userRepository: UserRepository,
) {

    @PostMapping
    fun saveNonConcurrent(@RequestBody user: User): User {
        return userRepository.save(user)
    }

    @GetMapping(params = ["name"])
    fun getByNameNonConcurrent(@RequestParam name: String): List<User> {
        return userRepository.findByName(name)
    }

    @GetMapping
    fun getNonConcurrent(): List<User> {
        return userRepository.findAll().toList()
    }
}