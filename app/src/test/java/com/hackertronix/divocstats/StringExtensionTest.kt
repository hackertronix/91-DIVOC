package com.hackertronix.divocstats

import org.junit.Assert
import org.junit.Test

class StringExtensionTest  {

    @Test
    fun `check parseDateToLong`() {
        val date = "2020-03-25"
        val expectedTimeStamp = 1585074600000
        Assert.assertEquals(expectedTimeStamp, date.parseDateToLong())
    }
}