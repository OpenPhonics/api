package com.openphonics.data.language

interface LanguageDao {
    fun create(nativeId: String, languageId: String, languageName: String, flag: String): Int
    fun all(nativeId: String): List<Language>
    fun update(id: Int, nativeId: String? = null, languageId: String? = null, languageName: String? = null, flag: String? = null): Int
    fun get(id: Int): Language?
    fun delete(id: Int): Boolean
}