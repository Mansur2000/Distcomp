package com.example.raw.dc.lab2.dao

import com.example.raw.dc.lab2.bean.Message

interface MessageDao {
	suspend fun create(item: Message): Long

	suspend fun deleteById(id: Long): Int

	suspend fun getAll(): List<Message?>

	suspend fun getById(id: Long): Message

	suspend fun update(item: Message): Int
}