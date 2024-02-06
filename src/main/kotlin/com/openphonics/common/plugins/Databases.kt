package com.openphonics.common.plugins

import com.openphonics.auth.User
import com.openphonics.auth.Users
import com.openphonics.common.DatabaseConfig
import com.openphonics.course.Courses
import com.openphonics.courseword.CourseWords
import com.openphonics.language.Languages
import com.openphonics.word.Words
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

fun Application.configureDatabase() {
    initDatabase(
        DatabaseConfig(
            host = environment.config.property("db.host").getString(),
            name = environment.config.property("db.name").getString(),
            user = environment.config.property("db.user").getString(),
            password = environment.config.property("db.password").getString(),
            maxPoolSize = environment.config.property("db.max_pool_size").getString().toInt()
        )
    )
}
private fun initDatabase(databaseConfig: DatabaseConfig) {
    val tables = arrayOf(
        Languages,
        Words,
        Courses,
        CourseWords,
        Users
        )

    Database.connect(createDataSource(databaseConfig))
    transaction {
        SchemaUtils.createMissingTablesAndColumns(*tables)
    }
}
private fun createDataSource(databaseConfig: DatabaseConfig): DataSource {
    val config = HikariConfig()
    with(databaseConfig) {
        config.jdbcUrl = "jdbc:postgresql://$host/$name"
        config.username = user
        config.password = password
        config.maximumPoolSize = maxPoolSize
    }
    config.validate()
    return HikariDataSource(config)
}