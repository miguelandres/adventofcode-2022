import utils.Direction
import utils.Position
import java.io.File

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
