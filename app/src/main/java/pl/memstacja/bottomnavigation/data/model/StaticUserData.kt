package com.pwszproducts.myapplication.data.model

class StaticUserData {

    companion object {
        var isLogin: Boolean = false
        lateinit var token: ResultLogin
        lateinit var user: ResultUser
    }

}