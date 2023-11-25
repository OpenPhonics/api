package com.openphonics

import com.openphonics.common.plugins.*
import io.ktor.server.application.*
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