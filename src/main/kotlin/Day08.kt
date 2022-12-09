import java.io.File

fun main() {
    val grid = File ("input/d08p01.txt").readLines().map{ line ->
        line.map{c -> c.code - '0'.code}.toIntArray()
    }.toTypedArray()

    val visibleGrid =Array(grid.size) { i -> Array(grid[i].size) { false} }
    for (i in grid.indices) {

        var maxSize = -1
        for (j in grid[i].indices) {
            if (grid[i][j]> maxSize) {
                visibleGrid[i][j] = true
                maxSize = grid[i][j]
            }
        }

        maxSize = -1
        for (j in grid[i].indices.reversed()) {
            if (grid[i][j]> maxSize) {
                visibleGrid[i][j] = true
                maxSize = grid[i][j]
            }
        }
    }

    for (j in 0 until grid[0].size) {

        var maxSize = -1
        for (i in grid.indices) {
            if (grid[i][j]> maxSize) {
                visibleGrid[i][j] = true
                maxSize = grid[i][j]
            }
        }

        maxSize = -1
        for (i in (grid.indices).reversed()) {
            if (grid[i][j]> maxSize) {
                visibleGrid[i][j] = true
                maxSize = grid[i][j]
            }
        }
    }

    println(visibleGrid.sumOf { list -> list.count { it } })
}