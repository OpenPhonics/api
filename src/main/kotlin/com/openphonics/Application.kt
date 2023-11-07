package com.openphonics

import com.openphonics.plugins.*
import io.ktor.server.application.*
import io.ktor.util.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDI()
    configureCORS()
    configureContentNegotiation()
    configureStatusPages()
    configureDatabase()
    configureRouting()
}

