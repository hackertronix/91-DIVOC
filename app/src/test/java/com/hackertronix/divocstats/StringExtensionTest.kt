package com.hackertronix.divocstats

import junit.framework.Assert.assertTrue
import org.joda.time.DateTime
import org.junit.Test

class StringExtensionTest {

    @Test
    fun `check parseDateToLong`() {
        val expectedDate = DateTime(2020, 3, 25, 0, 0, 0, 0)
        val actualDate = "2020-03-25".parseDateToLong()
        assertTrue(expectedDate.isEqual(actualDate))
    }
}