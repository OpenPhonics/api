package com.openphonics.plugins

import com.openphonics.data.DatabaseConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import javax.sql.DataSource

fun Application.configureDatabase() {
    initDatabase(
        DatabaseConfig(
            host = environment.config.property("db.host").getString(),
            port = environment.config.property("db.port").getString(),
            name = environment.config.property("db.name").getString(),
            user = environment.config.property("db.user").getString(),
            password = environment.config.property("db.password").getString(),
            driver = environment.config.property("db.driver").getString(),
            maxPoolSize = environment.config.property("db.max_pool_size").getString().toInt()
        )
    )
}
private fun initDatabase(databaseConfig: DatabaseConfig) {
    Database.connect(createDataSource(databaseConfig))
}
private fun createDataSource(databaseConfig: DatabaseConfig): DataSource {
    val config = HikariConfig()
    with(databaseConfig) {
        config.driverClassName = driver
        config.password = password
        config.jdbcUrl = "jdbc:postgresql://$host:$port/$name"
        config.maximumPoolSize = maxPoolSize
        config.username = user
    }
    config.validate()
    return HikariDataSource(config)
}