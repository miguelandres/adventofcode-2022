package utils

import kotlin.system.measureTimeMillis

fun wrapInTimeMeasurement(block: () -> Unit, description: String = "code block") {
    val time = measureTimeMillis { block() }
    println("$description took ${time}ms.")
}

fun <T>wrapInTimeMeasurementWithResult(block: () -> T, description: String = "code block"): T {
    var result: T
    val time = measureTimeMillis { result = block() }
    println("$description took ${time}ms.")
    return result
}
