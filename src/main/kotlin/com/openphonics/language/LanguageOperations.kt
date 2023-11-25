package com.openphonics.language

interface LanguageOperations<Base, MultiBase> {
    fun getByLanguageCode(code: String): Base
    fun getByLanguageName(name: String): Base
    fun all(): MultiBase
}
