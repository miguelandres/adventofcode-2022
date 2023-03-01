package utils

import kotlin.math.abs

fun extendRanges(ranges: Iterable<IntRange>) = ranges.minOf { it.first }..ranges.maxOf { it.last }

enum class Direction(val deltaX: Int, val deltaY: Int) {
    LEFT(-1, 0),
    RIGHT(1, 0),
    DOWN(0, -1),
    UP(0, 1),
}

enum class DirectionWithDiagonals(val deltaX: Int, val deltaY: Int) {
    LEFT(-1, 0),
    DOWN_LEFT(-1, -1),
    UP_LEFT(-1, 1),
    RIGHT(1, 0),
    DOWN_RIGHT(1, -1),
    UP_RIGHT(1, 1),
    DOWN(0, -1),
    UP(0, 1),
}

data class Position(val x: Int, val y: Int) {

    fun cartesianLineTo(other: Position): List<Position>? {
        return if (x == other.x) {
            (y.coerceAtMost(other.y)..y.coerceAtLeast(other.y)).map { i ->
                Position(x, i)
            }
        } else if (y == other.y) {
            (x.coerceAtMost(other.x)..x.coerceAtLeast(other.x)).map { i ->
                Position(i, y)
            }
        } else {
            null
        }
    }

    fun inRanges(rangeX: IntRange, rangeY: IntRange): Boolean {
        return rangeX.contains(x) && rangeY.contains(y)
    }

    fun manhattanDistance(other: Position) = abs(x - other.x) + abs(y - other.y)

    fun pointsAtManhattanDistance(distance: Int): Set<Position> {
        val rangeX = -distance..distance
        return rangeX.flatMap { deltaX ->
            val distanceY = distance - abs(deltaX)
            listOf(Position(x + deltaX, y - distanceY), Position(x + deltaX, y + distanceY))
        }.toSet()
    }

    fun adjustTail(headPosition: Position): Position {
        val deltaX = headPosition.x - x
        val deltaY = headPosition.y - y
        return if (deltaX in -1..1 && deltaY in -1..1) {
            this
        } else if (deltaX == 0) {
            Position(x, y + if (deltaY > 0) 1 else -1)
        } else if (deltaY == 0) {
            Position(x + if (deltaX > 0) 1 else -1, y)
        } else {
            Position(x + if (deltaX > 0) 1 else -1, y + if (deltaY > 0) 1 else -1)
        }
    }

    operator fun plus(other: Position): Position {
        return Position(x + other.x, y + other.y)
    }

    operator fun plus(direction: Direction): Position {
        return Position(x + direction.deltaX, y + direction.deltaY)
    }
    operator fun plus(other: DirectionWithDiagonals): Position {
        return Position(x + other.deltaX, y + other.deltaY)
    }
}
