package com.halogen.bit.model

import java.time.LocalDate

/**
 * This file shows all the extensions used
 */

/**
 * Converts String to Duration.
 * Format is HH:mm:ss
 * @receiver String
 * @return Duration
 */
fun String.toDuration() = run {
    val tokens = this.split(":")

    Duration(tokens[0].toInt(), tokens[1].toInt(), tokens[2].toInt())
}

/**
 * Reverses the Plan.toString() function
 * @receiver String
 * @return Plan
 */
fun String.toPlan() = run {
    val tokens = this.split(",")

    Plan(tokens[0], tokens[1].toDuration())
}