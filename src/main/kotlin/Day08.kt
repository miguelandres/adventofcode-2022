import java.io.File

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


fun main() {
    val grid = File("input/d08p01.txt").readLines().map { line ->
        line.map { c -> c.code - '0'.code }.toIntArray()
    }.toTypedArray()

    val rangeX = grid.indices
    val rangeY = grid[0].indices
    val visibleGrid = Array(grid.size) { i -> Array(grid[i].size) { false } }

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

    var maxScore = Int.MIN_VALUE
    for (i in rangeX) {
        for (j in rangeY) {
            maxScore = calculateScore(i, j, grid).coerceAtLeast(maxScore)
        }
    }
    println(maxScore)
}

fun calculateScore(x: Int, y: Int, grid: Array<IntArray>): Int {
    return Direction.values().map { direction -> calculateScore(x, y, grid, direction) }.reduce { a, b -> a * b }
}

fun calculateScore(
    x: Int, y: Int, grid: Array<IntArray>, direction: Direction
):
        Int {
    return calculateScoreForHeight(x + direction.deltaX, y + direction.deltaY, grid[x][y], grid, direction)
}

fun calculateScoreForHeight(x: Int, y: Int, height: Int, grid: Array<IntArray>, direction: Direction): Int {
    val rangeX = grid.indices
    val rangeY = grid[0].indices
    return if (!(x in rangeX && y in rangeY)) {
        0
    } else if (grid[x][y] >= height) {
        1
    } else {
        1 + calculateScoreForHeight(x + direction.deltaX, y + direction.deltaY, height, grid, direction)
    }
}
