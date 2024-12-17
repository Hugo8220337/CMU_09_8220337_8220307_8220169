package ipp.estg.cmu_09_8220169_8220307_8220337.utils

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class ConverterTest{
    private val converter = Converter()

    @Test
    fun fromStringList_convertsListToJsonString() {
        // Given
        val list = listOf("apple", "banana", "cherry")

        // When
        val result = converter.fromStringList(list)

        // Then
        val expectedJson = Gson().toJson(list)
        assertEquals(expectedJson, result)
    }

    @Test
    fun toStringList_convertsJsonStringToList() {
        // Given
        val json = """["apple","banana","cherry"]"""

        // When
        val result = converter.toStringList(json)

        // Then
        val expectedList = listOf("apple", "banana", "cherry")
        assertEquals(expectedList, result)
    }

    @Test
    fun fromStringList_handlesEmptyList() {
        // Given
        val emptyList = emptyList<String>()

        // When
        val result = converter.fromStringList(emptyList)

        // Then
        val expectedJson = Gson().toJson(emptyList)
        assertEquals(expectedJson, result)
    }

    @Test
    fun toStringList_handlesEmptyJsonString() {
        // Given
        val emptyJson = "[]"

        // When
        val result = converter.toStringList(emptyJson)

        // Then
        val expectedList = emptyList<String>()
        assertEquals(expectedList, result)
    }

    @Test
    fun fromStringList_handlesSpecialCharacters() {
        // Given
        val list = listOf("hello", "world", "🙂", "123", "\"quoted\"")

        // When
        val result = converter.fromStringList(list)

        // Then
        val expectedJson = Gson().toJson(list)
        assertEquals(expectedJson, result)
    }

    @Test
    fun toStringList_handlesSpecialCharacters() {
        // Given
        val json = """["hello","world","🙂","123","\"quoted\""]"""

        // When
        val result = converter.toStringList(json)

        // Then
        val expectedList = listOf("hello", "world", "🙂", "123", "\"quoted\"")
        assertEquals(expectedList, result)
    }

}