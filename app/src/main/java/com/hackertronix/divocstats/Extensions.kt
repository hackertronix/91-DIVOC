package com.hackertronix.divocstats

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

fun String.toFlagEmoji(): String {
    if (this.length != 2) {
        return this
    }

    val countryCodeCaps = this.toUpperCase() // upper case is important because we are calculating offset
    val firstLetter = Character.codePointAt(countryCodeCaps, 0) - 0x41 + 0x1F1E6
    val secondLetter = Character.codePointAt(countryCodeCaps, 1) - 0x41 + 0x1F1E6

    // 2. It then checks if both characters are alphabet
    if (!countryCodeCaps[0].isLetter() || !countryCodeCaps[1].isLetter()) {
        return this
    }

    return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
}

fun String.parseDate(): String {
    return DateTimeFormat.forPattern("dd MMM yyyy, hh:mm a ")
        .print(
            DateTime(this)
                .toLocalDateTime()
                .toDateTime().millis
        )
}

fun String.parseDateToLong(): Long {
    val formatter = DateTimeFormat.forPattern("yyyy-mm-dd")
    return formatter.parseMillis(this)
}
