package com.openphonics.data.flag

interface FlagDao {
    fun create(id: String, flag: String): String
    fun all(): List<Flag>
    fun update(id: String, newFlag: String): String
    fun get(id: String): Flag?
    fun delete(id: String): Boolean
    fun exists(id: String): Boolean
}