package com.openphonics.data.word

interface WordDao {
    fun create(language: Int, phonic: String, sound: String, translatedWord: String, translatedSound: String, word: String): Int
    fun update(
        id: Int,
        phonic: String? = null,
        sound: String? = null,
        translatedWord: String? = null,
        translatedSound: String? = null,
        word: String? = null
    ): Int
    fun get(id: Int): Word?
    fun get(language: Int, word: String): Word?
    fun all(language: Int): List<Word>
    fun delete(id: Int): Boolean
}