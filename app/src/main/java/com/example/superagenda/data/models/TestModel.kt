package com.example.superagenda.data.models

import com.example.superagenda.domain.models.Test
import org.bson.BsonDateTime
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

data class TestModel(val dateTime: BsonDateTime)

fun TestModel.toDomain() = Test(
    dateTime = bsonDateTimeToLocalDateTime(dateTime)
)

fun bsonDateTimeToLocalDateTime(bsonDateTime: BsonDateTime): LocalDateTime {
    val milliseconds = bsonDateTime.value
    val instant = Instant.ofEpochMilli(milliseconds)
    return LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
}

fun Test.toData() = TestModel(
    dateTime = localDateTimeToBsonDateTime(dateTime)
)

fun localDateTimeToBsonDateTime(localDateTime: LocalDateTime): BsonDateTime {
    val instant = localDateTime.toInstant(ZoneOffset.UTC)
    val milliseconds = instant.toEpochMilli()
    return BsonDateTime(milliseconds)
}