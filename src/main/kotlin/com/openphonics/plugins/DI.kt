package com.openphonics.plugins

import com.openphonics.application.di.component.AppComponent
import com.openphonics.application.di.component.ControllerComponent
import com.openphonics.application.di.component.DaggerAppComponent
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.util.*

@OptIn(KtorExperimentalAPI::class)
fun Application.configureDI() {

    val appComponent = DaggerAppComponent.builder().withApplication(this).build()

    attributes.put(appComponentKey, appComponent)
    attributes.put(controllerComponentKey, appComponent.controllerComponent())
}

@OptIn(KtorExperimentalAPI::class)
val controllerComponentKey = AttributeKey<ControllerComponent>("OPENPHONICS_CONTROLLER_COMPONENT")

@OptIn(KtorExperimentalAPI::class)
val appComponentKey = AttributeKey<AppComponent>("OPENPHONICS_APP_COMPONENT")

/**
 * Retrieves [ControllerComponent] from Application scope
 */
@OptIn(KtorExperimentalAPI::class)
val Application.controllers: ControllerComponent get() = attributes[controllerComponentKey]

/**
 * Retrieves [AppComponent] from Application scope
 */
@OptIn(KtorExperimentalAPI::class)
val Application.appComponent: AppComponent get() = attributes[appComponentKey]

/**
 * Retrieves [ControllerComponent] from Route scope
 */
@OptIn(KtorExperimentalAPI::class)
val Route.controllers: ControllerComponent get() = application.attributes[controllerComponentKey]