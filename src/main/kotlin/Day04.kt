import utils.parseRange
import java.io.File

fun main() {
    val assignments = File("input/d04p01.txt").readLines().map { s ->
        val parts = s.split(",").map { r -> parseRange(r) }
        Pair(parts[0], parts[1])
    }

    println(assignments.count { pair ->
        val intersectCount = pair.second.intersect(pair.first).count()
        intersectCount == pair.first.count() || intersectCount == pair.second.count()
    })

    println(assignments.count { pair -> pair.second.intersect(pair.first).isNotEmpty() })
}