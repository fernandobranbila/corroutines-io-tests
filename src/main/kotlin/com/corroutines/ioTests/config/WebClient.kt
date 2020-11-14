package com.corroutines.ioTests.config

import org.springframework.web.reactive.function.client.WebClient

object WebClient {

    fun get() = WebClient.create()

}