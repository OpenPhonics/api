package com.openphonics.common.exception

class NotFoundException(override val message: String) : Exception(message)
class BadRequestException(override val message: String) : Exception(message)
class UnauthorizedActivityException(override val message: String) : Exception(message)