package com.coroutines.ioTests

import com.coroutines.ioTests.digimon.entrypoint.DigimonHandler
import com.coroutines.ioTests.file.entrypoint.FilesHandler
import com.coroutines.ioTests.user.entrypoint.UserHandler
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

    @Bean
    fun userRoutes(userHandler: UserHandler) =
            coRouter {
                GET("/users-concurrent", queryParam("name") { it != null }, userHandler ::getByNameConcurrent)
                GET("/users-concurrent", userHandler::getAllConcurrent)
                POST("/users-concurrent", userHandler::saveConcurrent)
            }

}