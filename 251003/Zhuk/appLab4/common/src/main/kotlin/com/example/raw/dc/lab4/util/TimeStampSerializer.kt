package com.example.raw.dc.lab4.util

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.sql.Timestamp

@OptIn(ExperimentalSerializationApi::class)
@Serializer(Timestamp::class)
class TimeStampSerializer : KSerializer<Timestamp> {
	override fun deserialize(decoder: Decoder): Timestamp = Timestamp(decoder.decodeLong())

	override fun serialize(encoder: Encoder, value: Timestamp): Unit = encoder.encodeLong(value.time)
}