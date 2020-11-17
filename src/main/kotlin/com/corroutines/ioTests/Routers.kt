package com.corroutines.ioTests

import com.corroutines.ioTests.digimon.entrypoint.DigimonHandler
import com.corroutines.ioTests.file.entrypoint.FilesHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class Routers {

    @Bean
    fun digimonRoutes(digimonsHandler: DigimonHandler) =
            coRouter {
                GET("/digimons-async", digimonsHandler::getAllDigimonsAsync)
                GET("/digimons-sync", digimonsHandler::getAllDigimonsSync)
            }

    @Bean
    fun filesRoutes(filesHandler: FilesHandler) =
            coRouter {
                GET("/process-files-sync", filesHandler::processManyFilesSync)
                GET("/process-files-async", filesHandler::processManyFilesAsync)
            }

}