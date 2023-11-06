package com.openphonics.utils

fun String.isAlphaNumeric(): Boolean {
    return all { it.isLetterOrDigit() }
}
//fun String.isNumeric(): Boolean {
//    return !all { !it.isDigit() }
//}

fun String.containsOnlyLetters(): Boolean {

    return !this.contains(Regex("[0-9!\"#$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]"))
//    return this.matches(Regex("[\\p{L}]"))
}

fun String.isFullName(): Boolean {
    var whitespaceCount = 0

    for (char in this) {
        if (char.isWhitespace()) {
            whitespaceCount++
        } else if (!char.isLetter()) {
            return false
        }
    }

    return whitespaceCount == 1
}
