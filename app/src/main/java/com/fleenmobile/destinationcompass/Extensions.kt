package com.fleenmobile.destinationcompass

fun String.parseDouble(): Double? =
        if (isEmpty()) {
            null
        } else try {
            replace(",", ".").toDouble()
        } catch (e: NumberFormatException) {
            null
        }