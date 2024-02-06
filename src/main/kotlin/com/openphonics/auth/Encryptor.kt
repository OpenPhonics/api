package com.openphonics.auth

import com.openphonics.common.di.module.SecretKey
import io.ktor.util.hex
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

interface Encryptor {
    /**
     * Encrypts [data] and returns
     */
    fun encrypt(data: String): String
}

class OpEncryptor @Inject constructor(@SecretKey secret: String) : Encryptor {

    private val hmacKey: SecretKeySpec = SecretKeySpec(secret.toByteArray(), ALGORITHM)

    override fun encrypt(data: String): String {
        val hmac = Mac.getInstance(ALGORITHM)
        hmac.init(hmacKey)
        return hex(hmac.doFinal(data.toByteArray(Charsets.UTF_8)))
    }

    companion object {
        private const val ALGORITHM = "HmacSHA256"
    }
}