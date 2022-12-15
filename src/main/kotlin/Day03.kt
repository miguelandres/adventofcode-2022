import utils.readAocInput

fun main() {
    var lines = readAocInput(3)
    val packs = lines.map { line ->
        Pair(line.take(line.length / 2), line.substring(line.length / 2))
    }

    val priorities: List<Int> = packs.map { pair ->
        pair.first.toSet().intersect(pair.second.toSet()).first()
    }.map { c ->
        priority(c)
    }

    println(priorities.sum())


    /// Part 2

    var badgePriorities = 0
    while (!lines.isEmpty()) {
        val group: Set<Char> =
            lines.take(3).map { s -> s.toSet() }.reduce { s1: Set<Char>, s2: Set<Char> -> s1.intersect(s2) }
        lines = lines.drop(3)
        badgePriorities += priority(group.first())
    }
    println(badgePriorities)
}

private fun priority(c: Char) = if (c in 'a'..'z') {
    (c.code - 'a'.code) + 1
} else {
    (c.code - 'A'.code) + 27
}