package com.openphonics.plugins

import com.openphonics.application.route.FlagAPI
import com.openphonics.application.route.LanguageAPI
import com.openphonics.application.route.WordAPI
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.configureRouting() {
    install(Resources)
    routing {
        FlagAPI()
        LanguageAPI()
        WordAPI()
    }
}
