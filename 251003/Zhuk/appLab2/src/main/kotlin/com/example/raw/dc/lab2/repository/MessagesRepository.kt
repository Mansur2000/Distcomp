package com.example.raw.dc.lab2.repository

import com.example.raw.dc.lab2.bean.Message

interface MessagesRepository {
	suspend fun create(item: Message): Long?

	suspend fun getById(id: Long): Message?

	suspend fun getAll(): List<Message?>

	suspend fun update(item: Message): Boolean

	suspend fun deleteById(id: Long): Boolean
}