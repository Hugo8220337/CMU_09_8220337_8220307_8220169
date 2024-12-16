package ipp.estg.cmu_09_8220169_8220307_8220337.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class FormatTimeTest{

    @Test
    fun formatTime_zeroSeconds() {
        // Given
        val input = 0

        // When
        val result = formatTime(input)

        // Then
        val expected = "00:00"
        assertEquals(expected, result)
    }

    @Test
    fun formatTime_lessThanAMinute() {
        // Given
        val input = 45

        // When
        val result = formatTime(input)

        // Then
        val expected = "00:45"
        assertEquals(expected, result)
    }

    @Test
    fun formatTime_exactlyOneMinute() {
        // Given
        val input = 60

        // When
        val result = formatTime(input)

        // Then
        val expected = "01:00"
        assertEquals(expected, result)
    }

    @Test
    fun formatTime_multipleMinutesAndSeconds() {
        // Given
        val input = 125

        // When
        val result = formatTime(input)

        // Then
        val expected = "02:05"
        assertEquals(expected, result)
    }

    @Test
    fun formatTime_largeNumberOfSeconds() {
        // Given
        val input = 3605 // 1 hour and 5 seconds

        // When
        val result = formatTime(input)

        // Then
        val expected = "60:05" // 60 minutes = 1 hour
        assertEquals(expected, result)
    }

    @Test
    fun formatTime_boundaryCondition59Seconds() {
        // Given
        val input = 59

        // When
        val result = formatTime(input)

        // Then
        val expected = "00:59"
        assertEquals(expected, result)
    }

    @Test
    fun formatTime_boundaryConditionOneMinuteLessOneSecond() {
        // Given
        val input = 119

        // When
        val result = formatTime(input)

        // Then
        val expected = "01:59"
        assertEquals(expected, result)
    }

}