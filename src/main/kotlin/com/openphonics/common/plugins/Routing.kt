package com.openphonics.common.plugins


import com.openphonics.auth.AuthAPI
import com.openphonics.course.CourseAPI
import com.openphonics.courseword.CourseWordAPI
import com.openphonics.language.LanguageAPI
import com.openphonics.word.WordAPI
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import io.ktor.util.*

@OptIn(KtorExperimentalAPI::class)
fun Application.configureRouting() {
    install(Resources)
    routing {
        AuthAPI()
        LanguageAPI()
        WordAPI()
        CourseAPI()
        CourseWordAPI()
    }
}
