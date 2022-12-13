import java.io.File

abstract class DistressMessage : Comparable<DistressMessage> {
}

data class ListMessage(val list: List<DistressMessage>) : DistressMessage() {
    override fun compareTo(other: DistressMessage): Int {
        return when (other) {
            is IntMessage -> {
                this.compareTo(ListMessage(listOf(other)))
            }
            is ListMessage -> {
                this.list.zip(other.list).map { it.first.compareTo(it.second) }.find { it != 0 }
                    ?: (this.list.size - other.list.size)
            }
            else -> {
                throw NotImplementedError()
            }
        }
    }

    override fun toString(): String {
        return list.toString()
    }
}

data class IntMessage(val value: Int) : DistressMessage() {
    override fun compareTo(other: DistressMessage): Int {
        return when (other) {
            is IntMessage -> {
                value.compareTo(other.value)
            }
            is ListMessage -> {
                ListMessage(listOf(this)).compareTo(other)
            }
            else -> {
                throw NotImplementedError()
            }
        }
    }

    override fun toString(): String {
        return value.toString()
    }
}

fun main() {

    val list =
        File("input/d13p01.txt").readLines().asSequence().filter { it.isNotEmpty() }.map {
            parseListMessage(it).first
        }
    val pairList = list.chunked(2).toList().map { Pair(it[0], it[1]) }

    println(
        pairList.map { it.first.compareTo(it.second) }
            .mapIndexed { index, it -> if (it < 0) index + 1 else null }
            .filterNotNull().sum()
    )
    val sortedList = list.plus(sequenceOf(parseListMessage("[[2]]").first, parseListMessage("[[6]]").first))
        .sorted().map { it.toString() }.toList()
    val decoderKey =
        (sortedList.indexOf("[[2]]") + 1) * (sortedList.indexOf("[[6]]") + 1)
    println(decoderKey)

}


fun parseListMessage(s: String): Pair<ListMessage, Int> {
    // Remove [
    var currentIndex = 1
    var curr = s.drop(1)
    var list = listOf<DistressMessage>()
    // Found end of the
    while (!curr.startsWith("]")) {
        when (curr.first()) {
            ',' -> {
                currentIndex++
                curr = curr.drop(1)
            }
            else -> {
                val parsed = parseDistressMessage(curr)
                list = list.plus(parsed.first)
                currentIndex += parsed.second
                curr = curr.drop(parsed.second)
            }
        }
    }
    return Pair(ListMessage(list), currentIndex + 1)
}

fun parseIntMessage(s: String): Pair<IntMessage, Int> {
    val index = s.indexOfFirst { it !in '0'..'9' }
    val size = if (index < 0) s.length else index
    return Pair(IntMessage(s.substring(0, size).toInt()), size)
}

fun parseDistressMessage(s: String): Pair<DistressMessage, Int> {
    return when (s.first()) {
        '[' -> parseListMessage(s)
        else -> parseIntMessage(s)
    }
}
