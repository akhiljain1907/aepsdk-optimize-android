package com.adobe.marketing.mobile.optimize

import com.adobe.marketing.mobile.AdobeError
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class AEPOptimizeErrorTest {

    @Test
    fun `test toEventData without AdobeError`() {
        // Arrange
        val reportData: Map<String, Any> = mapOf("key1" to "value1", "key2" to "value2")
        val aepOptimizeError = AEPOptimizeError(
            type = "SomeErrorType",
            status = 100,
            title = "Not Found",
            detail = "The requested resource could not be found",
            report = reportData,
            adobeError = null
        )

        // Act
        val eventData = aepOptimizeError.toEventData()

        // Assert
        assertEquals("SomeErrorType", eventData[AEPOptimizeError.TYPE])
        assertEquals(100, eventData[AEPOptimizeError.STATUS])
        assertEquals("Not Found", eventData[AEPOptimizeError.TITLE])
        assertEquals(
            "The requested resource could not be found",
            eventData[AEPOptimizeError.DETAIL]
        )
        assertEquals(reportData, eventData[AEPOptimizeError.REPORT])
        //assertNull(eventData[AEPOptimizeError.ADOBE_ERROR])
    }

    @Test
    fun `test toEventData with AdobeError`() {
        // Arrange
        val adobeError = AdobeError.UNEXPECTED_ERROR
        val aepOptimizeError = AEPOptimizeError(
            type = "SomeErrorType",
            status = 500,
            title = "Internal Server Error",
            detail = "An unexpected error occurred",
            report = null,
            adobeError = adobeError
        )

        // Act
        val eventData = aepOptimizeError.toEventData()

        // Assert
        assertEquals("SomeErrorType", eventData[AEPOptimizeError.TYPE])
        assertEquals(500, eventData[AEPOptimizeError.STATUS])
        assertEquals("Internal Server Error", eventData[AEPOptimizeError.TITLE])
        assertEquals("An unexpected error occurred", eventData[AEPOptimizeError.DETAIL])
        assertNull(eventData[AEPOptimizeError.REPORT])
        assertNotNull(eventData[AEPOptimizeError.ADOBE_ERROR])

        val adobeErrorData = eventData[AEPOptimizeError.ADOBE_ERROR] as Map<String, Any?>
        assertEquals(adobeError.errorName, adobeErrorData[AEPOptimizeError.ERROR_NAME])
        assertEquals(adobeError.errorCode, adobeErrorData[AEPOptimizeError.ERROR_CODE])
    }

    @Test
    fun `test toAEPOptimizeError`() {
        // Arrange
        val data: Map<String, Any?> = mapOf(
            AEPOptimizeError.TYPE to "SomeErrorType",
            AEPOptimizeError.STATUS to 400,
            AEPOptimizeError.TITLE to "Bad Request",
            AEPOptimizeError.DETAIL to "Invalid request",
            AEPOptimizeError.REPORT to null,
            AEPOptimizeError.ADOBE_ERROR to mapOf(
                AEPOptimizeError.ERROR_NAME to AdobeError.UNEXPECTED_ERROR.errorName,
                AEPOptimizeError.ERROR_CODE to AdobeError.UNEXPECTED_ERROR.errorCode
            )
        )

        // Act
        val aepOptimizeError = AEPOptimizeError.toAEPOptimizeError(data)

        // Assert
        assertEquals("SomeErrorType", aepOptimizeError.type)
        assertEquals(400, aepOptimizeError.status)
        assertEquals("Bad Request", aepOptimizeError.title)
        assertEquals("Invalid request", aepOptimizeError.detail)
        assertNull(aepOptimizeError.report)
        assertEquals(AdobeError.UNEXPECTED_ERROR, aepOptimizeError.adobeError)
    }
}