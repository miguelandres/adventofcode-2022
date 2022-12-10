import java.io.File
import kotlin.math.abs

fun main() {
    val increases = File("input/d10p01.txt").readLines().flatMap {
        if (it == "noop") {
            sequenceOf(0)
        } else {
            sequenceOf(0, it.substring(5).toInt())
        }
    }

    var current = 1
    val valueDuringCycle = listOf<Int>(1).plus(increases.map {
        current += it
        current
    })
    val interestingCycles = sequenceOf(20, 60, 100, 140, 180, 220)

    val result = interestingCycles.sumOf { it * valueDuringCycle[it - 1] }

    println(result)

    val screen = (0 until 240).map { if (abs(valueDuringCycle[it] - (it % 40)) <= 1) '#' else '.' }.joinToString("")
    println(screen)
    (0 until 6).forEach { println(screen.substring(0 + it * 40, 40 + it * 40)) }

}