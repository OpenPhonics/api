package com.openphonics.plugins

import com.openphonics.application.di.component.AppComponent
import com.openphonics.application.di.component.ControllerComponent
import com.openphonics.application.di.component.DaggerAppComponent
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.util.*

fun Application.configureDI() {
    val appComponent = DaggerAppComponent.builder().withApplication(this).build()

    attributes.put(appComponentKey, appComponent)
    attributes.put(controllerComponentKey, appComponent.controllerComponent())
}

val controllerComponentKey = AttributeKey<ControllerComponent>("NOTY_CONTROLLER_COMPONENT")
val appComponentKey = AttributeKey<AppComponent>("NOTY_APP_COMPONENT")

/**
 * Retrieves [ControllerComponent] from Application scope
 */
val Application.controllers: ControllerComponent get() = attributes[controllerComponentKey]

/**
 * Retrieves [AppComponent] from Application scope
 */
val Application.appComponent: AppComponent get() = attributes[appComponentKey]

/**
 * Retrieves [ControllerComponent] from Route scope
 */
@OptIn(KtorExperimentalAPI::class)
val Route.controllers: ControllerComponent get() = application.attributes[controllerComponentKey]