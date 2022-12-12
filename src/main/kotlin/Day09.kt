import java.io.File

// Reusing Direction from day 8
data class Position(val x:Int, val y:Int) {
    fun move(direction: Direction): Position{
        return Position(x + direction.deltaX, y + direction.deltaY)
    }

    fun inRanges(rangeX: IntRange, rangeY:IntRange): Boolean {
        return rangeX.contains(x) && rangeY.contains(y)
    }
    fun adjustTail(headPosition: Position): Position {
        val deltaX = headPosition.x - x
        val deltaY = headPosition.y - y
        return if (deltaX in -1 .. 1 && deltaY in -1 .. 1) this
        else if (deltaX == 0) {
            Position(x, y + if (deltaY > 0) 1 else -1)
        } else if (deltaY == 0) {
            Position(x+ if (deltaX > 0) 1 else -1, y)
        } else {
            Position(x+ if (deltaX > 0) 1 else -1, y + if (deltaY > 0) 1 else -1)
        }
    }
}

fun main() {
    val instructions = File("input/d09p01.txt").readLines().map { line ->
        Pair (
            when(line[0]) {
                'R' -> Direction.RIGHT
                'L' -> Direction.LEFT
                'U' -> Direction.UP
                else -> Direction.DOWN
            },
            line.substring(2).toInt()
        )
    }

    var positionSet = calculateTailPositions(instructions,2)
    println(positionSet.count())

    positionSet = calculateTailPositions(instructions,10)
    println(positionSet.count())
}

private fun calculateTailPositions(instructions: List<Pair<Direction, Int>>, knots:Int): Set<Position> {
    val knotPosition = Array(knots) { Position(0, 0) }

    var positionSet = setOf(knotPosition.last())

    instructions.forEach { action ->
        for (x in 1..action.second) {
            knotPosition[0] = knotPosition[0].move(action.first)
            for (i in 1 until knots) {
                knotPosition[i] = knotPosition[i].adjustTail(knotPosition[i - 1])
            }
            positionSet = positionSet.plus(knotPosition.last())
        }

    }
    return positionSet
}
