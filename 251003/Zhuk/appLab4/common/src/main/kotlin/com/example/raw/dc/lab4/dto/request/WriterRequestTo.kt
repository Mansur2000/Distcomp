package com.example.raw.dc.lab4.dto.request

import com.example.raw.dc.lab4.bean.Writer
import kotlinx.serialization.Serializable

@Serializable
data class WriterRequestTo(
	private val login: String, private val password: String, private val firstname: String, private val lastname: String
) {
	fun toBean(id: Long?): Writer = Writer(id, login, password, firstname, lastname)

	init {
		require(login.length in 2..64)
		require(password.length in 8..128)
		require(firstname.length in 2..64)
		require(lastname.length in 2..64)
	}
}