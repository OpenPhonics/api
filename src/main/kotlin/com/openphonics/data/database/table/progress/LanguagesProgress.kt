package com.openphonics.data.database.table.progress

import com.openphonics.data.database.table.data.Languages
import com.openphonics.data.database.table.user.Users
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

object LanguagesProgress : UUIDTable("language_progress") {
    val user = reference("user", Users)
    val language = reference("language", Languages)
    val started = datetime("started").default(DateTime.now())
    var xp = integer("xp").default(0)
    var streak = integer("streak").default(0)
    var updated = datetime("updated").default(DateTime.now())

    init {
        uniqueIndex(user, language)
    }
}