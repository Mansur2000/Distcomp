package com.example.raw.dc.lab2.service.impl

import com.example.raw.dc.lab2.dto.request.MarkerRequestTo
import com.example.raw.dc.lab2.dto.request.MarkerRequestToId
import com.example.raw.dc.lab2.dto.response.MarkerResponseTo
import com.example.raw.dc.lab2.repository.MarkersRepository
import com.example.raw.dc.lab2.service.MarkerService

class MarkerServiceImpl(
	private val repository: MarkersRepository
) : MarkerService {
	override suspend fun create(requestTo: MarkerRequestTo?): MarkerResponseTo? {
		val bean = requestTo?.toBean(null) ?: return null
		val id = repository.create(bean) ?: return null
		val result = bean.copy(id = id)

		return result.toResponse()
	}

	override suspend fun deleteById(id: Long): Boolean = repository.deleteById(id)

	override suspend fun getAll(): List<MarkerResponseTo> {
		val result = repository.getAll()

		return result.filterNotNull().map { it.toResponse() }
	}

	override suspend fun getById(id: Long): MarkerResponseTo? {
		val result = repository.getById(id) ?: return null

		return result.toResponse()
	}

	override suspend fun update(requestTo: MarkerRequestToId?): MarkerResponseTo? {
		val bean = requestTo?.toBean() ?: return null

		if (!repository.update(bean)) {
			throw Exception("Exception during item updating!")
		}

		val result = bean.copy()

		return result.toResponse()
	}
}