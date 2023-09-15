package com.openphonics.route.auth


import com.openphonics.route.data.Data
import io.ktor.resources.*

object Routing {
    const val AUTH = "/auth"
    const val REGISTER = "register"
    const val LOGIN = "login"
    const val ADMIN ="admin"
    const val USER = "user"
    const val CLASS = "class"
    const val CLASS_CODE = "{classCode}"
    const val ID = "{id}"
}
@Resource(Routing.AUTH)
class Auth(){
    @Resource(Routing.REGISTER)
    class Register(val parent: Auth = Auth()){
        @Resource(Routing.ADMIN)
        class Admin(val parent: Register = Register())
    }
    @Resource(Routing.LOGIN)
    class Login(val parent: Auth = Auth())

    @Resource(Routing.ADMIN)
    class Admin(val parent: Auth = Auth()) {
        @Resource(Routing.CLASS)
        class Classroom(val parent: Admin = Admin()){

            @Resource(Routing.CLASS_CODE)
            class ClassCode(val parent: Classroom = Classroom(), val classCode: String)
        }
        @Resource(Routing.USER)
        class User(val parent: Admin = Admin()){
            @Resource(Routing.ID)
            class Id(val parent: User = User(), val id: String)
        }


    }





}

