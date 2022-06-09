package com.example

import com.example.tutorial.protos.Person
import com.google.protobuf.util.JsonFormat

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LibraryTest {
    private val printer = JsonFormat.printer().omittingInsignificantWhitespace()
    private val parser = JsonFormat.parser()

    @Test fun `基本的な toJson & fromJson 操作`() {
        val json = """{"name":"John Doe","id":1234,"email":"jdoe@example.com","phones":[{"number":"555-4321","type":"HOME"}]}"""

        // toJson
        val phoneNum = Person.PhoneNumber.newBuilder().setNumber("555-4321").setType(Person.PhoneType.HOME)
        val person = Person.newBuilder().setId(1234).setName("John Doe").setEmail("jdoe@example.com").addPhones(phoneNum).build()
        assertEquals(json, printer.print(person))

        // fromJson
        val builder = Person.newBuilder()
        parser.merge(json, builder)
        assertEquals("John Doe", builder.build().name)
    }

    @Test fun `field presence に関する printer の振る舞い`() {
        // 空の場合
        val personWithEmptyFields = Person.newBuilder().build()
        assertEquals("""{}""", printer.print(personWithEmptyFields))

        // デフォルト値の場合
        val personWithDefaultValues = Person.newBuilder().setId(0).setName("").setEmail("").build()
        assertEquals("""{"name":"","id":0,"email":""}""", printer.print(personWithDefaultValues))
    }

    @Test fun `field presence に関する includingDefaultValueFields オプションの振る舞い`() {
        val customPrinter = printer.includingDefaultValueFields()

        // 空の場合
        val personWithEmptyFields = Person.newBuilder().build()
        assertEquals("""{"phones":[]}""", customPrinter.print(personWithEmptyFields))

        val personWithDefaultValues = Person.newBuilder().setId(0).setName("").setEmail("").build()
        assertEquals("""{"name":"","id":0,"email":""}""", printer.print(personWithDefaultValues))
    }

    @Test fun `field presence に関する parser の振る舞い`() {
        // 空の場合
        val personFromEmptyObj = run {
            val builder = Person.newBuilder()
            parser.merge("""{}""", builder)
            builder.build()
        }
        assertFalse(personFromEmptyObj.hasId())
        assertFalse(personFromEmptyObj.hasEmail())
        assertEquals(0, personFromEmptyObj.id) // なんでだよ！
        assertEquals("", personFromEmptyObj.email) // なんでだよ！

        // デフォルト値の場合
        val personFromDefaultVals = run {
            val builder = Person.newBuilder()
            parser.merge("""{"name":"","id":0,"email":""}""", builder)
            builder.build()
        }
        assertTrue(personFromDefaultVals.hasId())
        assertTrue(personFromDefaultVals.hasEmail())
        assertEquals(0, personFromDefaultVals.id)
        assertEquals("", personFromDefaultVals.email)
    }

    @Test fun `JSON関係なく、getter は常に default value を返す`() {
        val person = Person.newBuilder().build()
        assertEquals(0, person.id) // なんでだよ！
        assertEquals("", person.email) // なんでだよ！
    }
}
