package utils

open class SparseMap<T>(private val defaultValue: T) {

    val map = HashMap<Position, T>()

    open operator fun get(pos: Position) = map[pos] ?: defaultValue

    operator fun set(pos: Position, value: T) {
        map[pos] = value
    }

    open fun rangeX(): IntRange {
        val sortedCoords = map.keys.map { it.x }.sorted()
        return sortedCoords.first()..sortedCoords.last()
    }

    open fun rangeY(): IntRange {
        val sortedCoords = map.keys.map { it.y }.sorted()
        return sortedCoords.first()..sortedCoords.last()
    }
}

class DistanceAwareSparseMap<T>(private val defaultValue: T) : SparseMap<T>(defaultValue) {

    private val mapDistanceAwareDefaults = HashMap<Position, Pair<Int, T>>()

    private fun generateDefault(pos: Position) =
        mapDistanceAwareDefaults.entries.find { entry -> entry.key.manhattanDistance(pos) <= entry.value.first }
            ?.value?.second
            ?: defaultValue

    fun addDistanceAwareDefault(pos: Position, distance: Int, value: T) {
        mapDistanceAwareDefaults[pos] = Pair(distance, value)
    }

    fun copyMapDistanceAwareDefaults() = mapDistanceAwareDefaults.toMap()

    private fun distanceAwareDefaultRangeX(): IntRange? {
        if (mapDistanceAwareDefaults.isEmpty()) return null
        val ranges = mapDistanceAwareDefaults.entries.map { entry ->
            entry.key.x - entry.value.first..entry.key.x + entry.value.first
        }
        return extendRanges(ranges)
    }

    private fun distanceAwareDefaultRangeY(): IntRange? {
        if (mapDistanceAwareDefaults.isEmpty()) return null
        val ranges = mapDistanceAwareDefaults.entries.map { entry ->
            entry.key.y - entry.value.first..entry.key.y + entry.value.first
        }
        return extendRanges(ranges)
    }

    override fun rangeX(): IntRange {
        return extendRanges(listOfNotNull(super.rangeX(), distanceAwareDefaultRangeX()))
    }

    override fun rangeY(): IntRange {
        return extendRanges(listOfNotNull(super.rangeY(), distanceAwareDefaultRangeY()))
    }

    override fun get(pos: Position) = map[pos] ?: generateDefault(pos)
}
