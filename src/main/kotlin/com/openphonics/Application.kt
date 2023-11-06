package com.openphonics

import com.openphonics.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureCORS()
    configureContentNegotiation()
    configureStatusPages()
    configureDatabase()
    configureRouting()
}
