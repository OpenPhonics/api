package com.openphonics

import com.openphonics.common.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    (environment as ApplicationEngineEnvironment).connectors.forEach { connector ->
        println("${connector.host}:${connector.port}")
    }
    configureDI()
    configureCORS()
    configureContentNegotiation()
    configureStatusPages()
    configureDatabase()
    configureRouting()
}