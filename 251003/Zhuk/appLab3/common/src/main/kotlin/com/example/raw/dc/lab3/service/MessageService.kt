package com.example.raw.dc.lab3.service

import com.example.raw.dc.lab3.dto.request.MessageRequestTo
import com.example.raw.dc.lab3.dto.request.MessageRequestToId
import com.example.raw.dc.lab3.dto.response.MessageResponseTo

interface MessageService {
	suspend fun create(requestTo: MessageRequestTo?): MessageResponseTo?

	suspend fun deleteById(id: Long): Boolean

	suspend fun getAll(): List<MessageResponseTo>

	suspend fun getById(id: Long): MessageResponseTo?

	suspend fun update(requestTo: MessageRequestToId?): MessageResponseTo?
}