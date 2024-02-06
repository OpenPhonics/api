package com.openphonics.common.plugins

import com.openphonics.auth.JWTController
import com.openphonics.auth.OpJWTController
import com.openphonics.auth.UserDao
import com.openphonics.auth.principal.UserPrincipal
import dagger.Lazy
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.util.KtorExperimentalAPI
import java.util.UUID

@KtorExperimentalAPI
fun Application.configureAuthentication(
    jwtController: Lazy<JWTController> = appComponent.controllerComponent().jwtController(),
    userDao: Lazy<UserDao> = appComponent.daoComponent().userDao()
) {
    install(Authentication) {
        jwt {
            verifier(jwtController.get().verifier)
            validate {
                // Extract userId from token
                val userId = it.payload.getClaim(OpJWTController.ClAIM).asString()

                // Return Principle if user exists otherwise null
                val user = userDao.get().findByUUID(UUID.fromString(userId))
                if (user != null) UserPrincipal(user) else null
            }
        }
    }
}