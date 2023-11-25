package com.openphonics.word

interface WordOperations<Base, MultiBase> {
    fun get(word: String, language: Int): Base
    fun all(language: Int): MultiBase
}
