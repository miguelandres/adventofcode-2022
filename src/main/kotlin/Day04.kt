import utils.parseRange
import utils.readAocInput

fun main() {
    val assignments = readAocInput(4).map { s ->
        val parts = s.split(",").map { r -> parseRange(r) }
        Pair(parts[0], parts[1])
    }

    println(
        assignments.count { pair ->
            val intersectCount = pair.second.intersect(pair.first).count()
            intersectCount == pair.first.count() || intersectCount == pair.second.count()
        }
    )

    println(assignments.count { pair -> pair.second.intersect(pair.first).isNotEmpty() })
}
