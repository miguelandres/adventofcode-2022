import utils.Direction
import utils.Position
import utils.readAocInput
import utils.wrapInTimeMeasurement
import utils.wrapInTimeMeasurementWithResult

fun main() {
    val instructions = wrapInTimeMeasurementWithResult({
        readAocInput(9).map { line ->
            Pair(
                when (line[0]) {
                    'R' -> Direction.RIGHT
                    'L' -> Direction.LEFT
                    'U' -> Direction.UP
                    else -> Direction.DOWN
                },
                line.substring(2).toInt(),
            )
        }
    }, "parse")

    wrapInTimeMeasurement({
        val positionSet = calculateTailPositions(instructions, 2)
        println(positionSet.count())
    }, "part1")

    wrapInTimeMeasurement({
        val positionSet = calculateTailPositions(instructions, 10)
        println(positionSet.count())
    }, "part2")
}

private fun calculateTailPositions(instructions: List<Pair<Direction, Int>>, knots: Int): Set<Position> {
    val knotPosition = Array(knots) { Position(0, 0) }

    var positionSet = setOf(knotPosition.last())

    instructions.forEach { action ->
        for (x in 1..action.second) {
            knotPosition[0] = knotPosition[0] + action.first
            for (i in 1 until knots) {
                knotPosition[i] = knotPosition[i].adjustTail(knotPosition[i - 1])
            }
            positionSet = positionSet.plus(knotPosition.last())
        }
    }
    return positionSet
}
