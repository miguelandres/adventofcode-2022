import java.io.File

fun findStartOfSignal(line: String, length: Int = 4): Int {
    for (i in 0 until line.length - length) {
        if (line.subSequence(i, i + length).toSet().count() == length) return i + length
    }
    return 0
}

fun main() {
    var line = File("input/d06p01.txt").readLines().first()

    println(findStartOfSignal(line))
    println(findStartOfSignal(line, 14))
}