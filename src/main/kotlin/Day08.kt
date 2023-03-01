import utils.Direction
import utils.Position
import utils.readAocInput
import utils.wrapInTimeMeasurement

fun main() {
    val grid = readAocInput(8).map { line ->
        line.map { c -> c.code - '0'.code }.toIntArray()
    }.toTypedArray()

    val rangeX = grid.indices
    val rangeY = grid[0].indices
    val visibleGrid = Array(grid.size) { i -> Array(grid[i].size) { false } }

    wrapInTimeMeasurement({
        for (i in grid.indices) {
            var maxSize = -1
            for (j in grid[i].indices) {
                if (grid[i][j] > maxSize) {
                    visibleGrid[i][j] = true
                    maxSize = grid[i][j]
                }
            }

            maxSize = -1
            for (j in grid[i].indices.reversed()) {
                if (grid[i][j] > maxSize) {
                    visibleGrid[i][j] = true
                    maxSize = grid[i][j]
                }
            }
        }

        for (j in 0 until grid[0].size) {
            var maxSize = -1
            for (i in grid.indices) {
                if (grid[i][j] > maxSize) {
                    visibleGrid[i][j] = true
                    maxSize = grid[i][j]
                }
            }

            maxSize = -1
            for (i in (grid.indices).reversed()) {
                if (grid[i][j] > maxSize) {
                    visibleGrid[i][j] = true
                    maxSize = grid[i][j]
                }
            }
        }

        println(visibleGrid.sumOf { list -> list.count { it } })
    }, "part1")
    wrapInTimeMeasurement({
        var maxScore = Int.MIN_VALUE
        for (i in rangeX) {
            for (j in rangeY) {
                maxScore = calculateScore(Position(i, j), grid).coerceAtLeast(maxScore)
            }
        }
        println(maxScore)
    }, "part2")
}

fun calculateScore(pos: Position, grid: Array<IntArray>): Int {
    return Direction.values().map { direction -> calculateScore(pos, grid, direction) }.reduce { a, b -> a * b }
}

fun calculateScore(
    pos: Position,
    grid: Array<IntArray>,
    direction: Direction,
):
    Int {
    return calculateScoreForHeight(pos + direction, grid[pos.x][pos.y], grid, direction)
}

fun calculateScoreForHeight(pos: Position, height: Int, grid: Array<IntArray>, direction: Direction): Int {
    val rangeX = grid.indices
    val rangeY = grid[0].indices
    return if (!(pos.inRanges(rangeX, rangeY))) {
        0
    } else if (grid[pos.x][pos.y] >= height) {
        1
    } else {
        1 + calculateScoreForHeight(pos + direction, height, grid, direction)
    }
}
