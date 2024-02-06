package com.openphonics.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.openphonics.common.di.module.SecretKey
import javax.inject.Inject

interface JWTController {


    val verifier: JWTVerifier
    /**
     * Generates JWT Token from [data]
     */
    fun sign(data: String): String
}

/**
 * Simple implementation for providing JWT Authentication mechanism.
 * Use [sign] method to generate token.
 */
class OpJWTController @Inject constructor(@SecretKey secret: String) : JWTController {
    private val algorithm = Algorithm.HMAC256(secret)
    override val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .build()

    /**
     * Generates JWT token from [userId].
     */
    override fun sign(data: String): String = JWT
        .create()
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .withClaim(ClAIM, data)
        .sign(algorithm)

    companion object {
        private const val ISSUER = "NotyKT-JWT-Issuer"
        private const val AUDIENCE = "https://notykt-production.up.railway.app"
        const val ClAIM = "userId"
    }
}