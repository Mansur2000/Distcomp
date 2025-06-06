package com.example.eger.dc.lab5.controller.routing

import com.example.eger.dc.lab5.dto.request.MessageRequestTo
import com.example.eger.dc.lab5.dto.request.MessageRequestToId
import com.example.eger.dc.lab5.dto.response.MessageResponseTo
import com.example.eger.dc.lab5.sendViaKafka
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

internal fun Route.messagesRouting() {
	val client = HttpClient(CIO) {
		install(ContentNegotiation) {
			json()
		}
	}

	route("/messages") {
		checkMessages(client)

		createMessage(client)
		deleteMessage(client)
		updateMessage(client)
		getMessage(client)
	}
}

private fun Route.checkMessages(client: HttpClient) {
	get {
		call.respond(
			client.get("http://0.0.0.0:24130/api/v1.0/messages").bodyAsText()
		)

		sendViaKafka("From Publisher: Messages GET [redirect]")
	}
}

private fun Route.createMessage(client: HttpClient) {
	post {
		val body = call.receive<MessageRequestTo>()
		fromCache = body.content
		val result = client.post("http://localhost:24130/api/v1.0/messages") {
			contentType(ContentType.Application.Json)
			setBody(body)
		}
		call.respond(
			status = result.status, message = result.bodyAsText()
		)

		sendViaKafka("From Publisher: Messages POST [redirect]")
	}
}

private fun Route.getMessage(client: HttpClient) {
	get("/{id?}") {
		ask++
		val id = call.parameters["id"]
		val result = client.get("http://localhost:24130/api/v1.0/messages/$id")
		val body = result.body<MessageResponseTo>()
		doCaching(body)
		call.respond(
			status = result.status, message = body
		)
		sendViaKafka("From Publisher: Messages GET ID [redirect]")
	}
}

private fun Route.deleteMessage(client: HttpClient) {
	delete("/{id?}") {
		val id = call.parameters["id"]
		val result = client.delete("http://localhost:24130/api/v1.0/messages/$id")
		call.respond(
			status = result.status, message = result.bodyAsText()
		)

		sendViaKafka("From Publisher: Messages DELETE ID [redirect]")
	}
}

private fun Route.updateMessage(client: HttpClient) {
	put {
		val body = call.receive<MessageRequestToId>()
		val result = client.put("http://localhost:24130/api/v1.0/messages") {
			contentType(ContentType.Application.Json)
			setBody(body)
		}
		call.respond(
			status = result.status, message = result.bodyAsText()
		)

		sendViaKafka("From Publisher: Labels PUT [redirect]")
	}
}

var ask: Int = 0
var fromCache: String = ""

private fun doCaching(body: MessageResponseTo) {
	if (ask == 2) body.content = fromCache
	if (ask == 4) ask = 0
}