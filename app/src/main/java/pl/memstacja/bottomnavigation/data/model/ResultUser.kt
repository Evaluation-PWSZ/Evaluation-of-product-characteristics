package com.pwszproducts.myapplication.data.model

data class ResultUser (
    var id: Int,
    var login: String,
    var email: String,
    var email_verified_at: String?,
    var created_at: String,
    var updated_at: String
)