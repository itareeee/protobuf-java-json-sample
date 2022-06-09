package com.example

import com.example.tutorial.protos.Person
import com.google.protobuf.util.JsonFormat
import kotlin.test.Test
import kotlin.test.assertEquals

class LibraryTest {
    private val json = """{"name":"John Doe","id":1234,"email":"jdoe@example.com","phones":[{"number":"555-4321","type":"HOME"}]}"""

    private val person = run {
        val phoneNum = Person.PhoneNumber.newBuilder()
                .setNumber("555-4321")
                .setType(Person.PhoneType.HOME)

        Person.newBuilder()
                .setId(1234)
                .setName("John Doe")
                .setEmail("jdoe@example.com")
                .addPhones(phoneNum)
                .build()
    }

    @Test fun toJson() {
        val printer = JsonFormat.printer()
                .includingDefaultValueFields()
                .omittingInsignificantWhitespace()

        assertEquals(json, printer.print(person))
    }

    @Test fun fromJson() {
        val builder = Person.newBuilder()
        JsonFormat.parser().merge(json, builder)
        val person = builder.build()

        assertEquals("John Doe", person.name)
    }
}
