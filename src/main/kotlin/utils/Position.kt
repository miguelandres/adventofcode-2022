package utils

enum class Direction(val deltaX: Int, val deltaY: Int) {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1)
}

enum class DirectionWithDiagonals(val deltaX: Int, val deltaY: Int) {
    UP(-1, 0),
    UP_LEFT(-1, -1),
    UP_RIGHT(-1, 1),
    DOWN(1, 0),
    DOWN_LEFT(1, -1),
    DOWN_RIGHT(1, 1),
    LEFT(0, -1),
    RIGHT(0, 1)
}

data class Position(val x: Int, val y: Int) {
    fun move(direction: Direction): Position {
        return Position(x + direction.deltaX, y + direction.deltaY)
    }

    fun moveWithDiagonal(direction: DirectionWithDiagonals): Position {
        return Position(x + direction.deltaX, y + direction.deltaY)
    }

    fun cartesianLineTo(other: Position): List<Position>? {
        return if (x == other.x) {
            (y.coerceAtMost(other.y)..y.coerceAtLeast(other.y)).map { i ->
                Position(x, i)
            }
        } else if (y == other.y) {
            (x.coerceAtMost(other.x)..x.coerceAtLeast(other.x)).map { i ->
                Position(i, y)
            }
        } else null
    }

    fun inRanges(rangeX: IntRange, rangeY: IntRange): Boolean {
        return rangeX.contains(x) && rangeY.contains(y)
    }

    fun adjustTail(headPosition: Position): Position {
        val deltaX = headPosition.x - x
        val deltaY = headPosition.y - y
        return if (deltaX in -1..1 && deltaY in -1..1) this
        else if (deltaX == 0) {
            Position(x, y + if (deltaY > 0) 1 else -1)
        } else if (deltaY == 0) {
            Position(x + if (deltaX > 0) 1 else -1, y)
        } else {
            Position(x + if (deltaX > 0) 1 else -1, y + if (deltaY > 0) 1 else -1)
        }
    }
}
