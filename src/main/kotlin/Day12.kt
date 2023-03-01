import utils.Direction
import utils.Position
import utils.readAocInput
import utils.wrapInTimeMeasurement
import utils.wrapInTimeMeasurementWithResult

fun main() {
    var start = Position(0, 0)
    var end = Position(0, 0)
    val grid =
        wrapInTimeMeasurementWithResult({
            readAocInput(12).mapIndexed { x, line ->
                line.mapIndexed { y, c ->
                    when (c) {
                        'S' -> {
                            start = Position(x, y)
                            0
                        }
                        'E' -> {
                            end = Position(x, y)
                            'z'.code - 'a'.code
                        }
                        else -> {
                            c.code - 'a'.code
                        }
                    }
                }.toList()
            }.toList()
        }, "parse")

    wrapInTimeMeasurement({
        println(findBestPath(grid, start, { from, to -> to < from + 2 }) { pos -> pos == end })
    }, "part1")
    wrapInTimeMeasurement({
        println(findBestPath(grid, end, { from, to -> from < to + 2 }) { pos -> grid[pos.x][pos.y] == 0 })
    }, "part2")
}

private fun findBestPath(
    grid: List<List<Int>>,
    start: Position,
    canMoveToElevationFn: (Int, Int) -> Boolean,
    endFn: (Position) -> Boolean,
): Int {
    val rangeX = grid.indices
    val rangeY = grid[0].indices
    val costGrid = Array(grid.size) { Array(grid[0].size) { Int.MAX_VALUE } }
    var queue = listOf(Pair(start, 0))

    while (queue.isNotEmpty()) {
        val curr = queue.first()
        val pos = curr.component1()
        val cost = curr.component2()
        queue = queue.drop(1)
        if (costGrid[pos.x][pos.y] > cost) {
            costGrid[pos.x][pos.y] = cost
            if (endFn(pos)) {
                return cost
            }
            queue = queue.plus(
                Direction.values().map { pos.move(it) }
                    .filter { it.inRanges(rangeX, rangeY) }
                    .filter { canMoveToElevationFn(grid[pos.x][pos.y], grid[it.x][it.y]) }
                    .map { Pair(it, cost + 1) },
            )
        }
    }
    return 0
}
