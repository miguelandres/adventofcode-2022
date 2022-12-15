package utils

import java.io.File

fun parseRange(s: String): IntRange {
    val parts = s.split("-").map { x -> x.toInt() }
    return parts[0]..parts[1]
}

private fun padInputNumber(n: Int) = n.toString().padStart(2, '0')

fun readAocInput(day: Int, part: Int = 1) =
    File("input/d${padInputNumber(day)}p${padInputNumber(part)}.txt").readLines()

fun splitWithPrefix(s: String, prefix: String, delimiter: String) = s.drop(prefix.length).split(delimiter)
