package com.openphonics.common

class DatabaseConfig(
    val host: String,
//    val port: String,
    val name: String,
    val user: String,
    val password: String,
    val driver: String,
    val maxPoolSize: Int
)