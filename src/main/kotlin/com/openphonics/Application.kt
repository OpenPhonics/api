package com.openphonics

import com.openphonics.common.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.util.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@OptIn(KtorExperimentalAPI::class)
fun Application.module() {
    (environment as ApplicationEngineEnvironment).connectors.forEach { connector ->
        println("${connector.host}:${connector.port}")
    }

    configureDI()
    configureAuthentication()
    configureCORS()
    configureContentNegotiation()
    configureStatusPages()
    configureDatabase()
    configureRouting()
}