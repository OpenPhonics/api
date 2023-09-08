package com.openphonics.route.auth


import io.ktor.resources.*

object Routing {
    const val AUTH = "/auth"
    const val REGISTER = "register"
    const val LOGIN = "login"
    const val ADMIN ="admin"
    const val CLASS = "class"
}
@Resource(Routing.AUTH)
class Auth(){
    @Resource(Routing.REGISTER)
    class Register(val parent: Auth = Auth()){
        @Resource(Routing.ADMIN)
        class Admin(val parent: Register = Register())
    }
    @Resource(Routing.CLASS)
    class Class(val parent: Auth = Auth())

    @Resource(Routing.LOGIN)
    class Login(val parent: Auth = Auth())

}

