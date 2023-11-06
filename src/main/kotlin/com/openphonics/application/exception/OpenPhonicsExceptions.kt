package com.openphonics.application.exception

class NotFoundException(override val message: String) : Exception(message)
class BadRequestException(override val message: String) : Exception(message)