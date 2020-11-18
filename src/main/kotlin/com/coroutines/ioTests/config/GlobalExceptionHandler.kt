package com.coroutines.ioTests.config

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.logging.Logger

@ControllerAdvice
class GlobalExceptionHandler {

    companion object{
        private val LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }


    @ExceptionHandler(Exception::class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error processing the request, please check the logs")
    fun handleException(e: Exception?) {
        LOGGER.error("Exception occurred", e)
    }
}