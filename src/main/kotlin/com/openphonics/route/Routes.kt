package com.openphonics.route


val ROUTE_VAR = { str: String -> "/{$str}" }
fun ROUTE_VARS(vararg strings: String): String {
    return strings.joinToString("") { ROUTE_VAR(it) }
}

val ROUTE = { str: String -> "/$str" }

//GENERAL ROUTES
const val CREATE = "new"
const val UNITS = "units"
const val SECTIONS = "sections"

const val AUTH = "/auth"
const val REGISTER = "register"
const val LOGIN = "login"

//PROGRESS ROUTES
const val PROGRESS = "progress"
const val STREAK = "streak"
const val LANGUAGE_PROGRESS_ID = "languageProgressId"
const val UNIT_PROGRESS_ID = "unitProgressId"
const val SECTION_PROGRESS_ID = "sectionProgressId"

//DATA ROUTES
const val DATA = "data"
const val LANGUAGE = "language"
const val LANGUAGE_ID = "languageId"