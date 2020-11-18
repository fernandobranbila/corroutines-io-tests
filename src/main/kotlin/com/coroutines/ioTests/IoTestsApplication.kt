package com.coroutines.ioTests

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
class IoTestsApplication

fun main(args: Array<String>) {
	runApplication<IoTestsApplication>(*args)
}
