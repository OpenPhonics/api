/*
 * Copyright 2020 Shreyas Patil
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.openphonics.data.database.table.user

import com.openphonics.data.database.table.progress.LanguagesProgress
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object Users : UUIDTable() {
    val name = varchar("name", length = 30)
    val classCode = reference("class_code", Classrooms, onDelete = ReferenceOption.CASCADE)
    val native = char("native", length = 2)
    var currentLanguage = reference("current_language", LanguagesProgress).nullable()
    var isAdmin = bool("admin").default(false)
}