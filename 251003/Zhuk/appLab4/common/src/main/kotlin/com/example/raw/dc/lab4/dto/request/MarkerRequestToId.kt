package com.example.raw.dc.lab4.dto.request

import com.example.raw.dc.lab4.bean.Marker
import kotlinx.serialization.Serializable

@Serializable
data class MarkerRequestToId(
	private val id: Long, private val name: String
) {
	fun toBean(): Marker = Marker(
		id, name
	)

	init {
		require(name.length in 2..32)
	}
}