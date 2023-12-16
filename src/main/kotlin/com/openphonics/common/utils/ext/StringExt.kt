package com.openphonics.common.utils.ext

fun String.containsOnlyLetters(): Boolean {

    return !this.contains(Regex("[0-9!\"#$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]"))
//    return this.matches(Regex("[\\p{L}]"))
}