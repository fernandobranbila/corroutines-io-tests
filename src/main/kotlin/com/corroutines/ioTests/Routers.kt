package com.corroutines.ioTests

import com.corroutines.ioTests.digimon.entrypoint.DigimonHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class Routers {

    @Bean
    fun routes(publicationHandler: DigimonHandler) =
            coRouter {
                GET("/digimonsAsync", publicationHandler::getAllDigimonsAsync)
                GET("/digimonsSync", publicationHandler::getAllDigimonsSync)
            }

}