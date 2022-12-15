package utils

fun parseRange(s: String): IntRange {
    val parts = s.split("-").map { x -> x.toInt() }
    return parts[0]..parts[1]
}