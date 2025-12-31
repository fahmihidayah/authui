package com.fahmi.authui.data.request

import com.google.gson.annotations.SerializedName

data class LoginForm(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
