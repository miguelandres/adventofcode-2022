import java.io.File


fun parsePosition(s: String, delimiter: String = ","): Position {
    val parts = s.split(delimiter)
    return Position(parts[0].toInt(), parts[1].toInt())
}

fun main() {

    val pointSet =
        File("input/d14p01.txt").readLines().flatMap {
            val listPositions = it.split(" -> ").map { pos -> parsePosition(pos) }
            val pairs = listPositions.dropLast(1).zip(listPositions.drop(1))
            pairs.flatMap { pair ->
                pair.first.cartesianLineTo(pair.second)!!
            }.toHashSet()
        }.toHashSet()


    val grid = createGrid(pointSet)
    val maxDepth = pointSet.maxOf { it.y }
    val count = simulate(grid, maxDepth)
    grid.print()
    println(count)

    val gridWithFloor = createGrid(pointSet, true)
    val countWithFloor = simulate(gridWithFloor)
    gridWithFloor.print()
    println(countWithFloor)
}

private fun simulate(grid: CaveGrid, maxDepth: Int? = null): Int {
    var count = -1

    val initialPosition= Position(500,0)
    do {
        val passedMaxDepth = simulateFall(grid, maxDepth)
        count++
    } while (!passedMaxDepth && grid[initialPosition] == CaveContents.AIR)
    return if (maxDepth == null) count + 1 else count
}

private fun createGrid(pointSet: HashSet<Position>, withFloor: Boolean = false): CaveGrid {
    val floorDepth = pointSet.maxOf { it.y } + 2
    return CaveGrid(
        HashMap(pointSet.associateWith { CaveContents.ROCK }),
        if (withFloor)
            floorDepth else null
    )
}



enum class CaveContents {
    AIR,
    ROCK,
    SAND
}

class CaveGrid(private val grid: HashMap<Position, CaveContents>, private val floor: Int?) {
    operator fun get(position: Position): CaveContents {
        return if (floor != null && position.y == floor) CaveContents.ROCK
        else grid[position] ?: CaveContents.AIR
    }

    operator fun set(position: Position, value: CaveContents) {
        grid[position] = value
    }

    private fun rangeOfActiveCells(): Pair<IntRange, IntRange> {
        val filteredGrid = grid.filter { it.value != CaveContents.AIR }
        val sortedX = filteredGrid.keys.sortedBy { it.x }
        val sortedY = filteredGrid.keys.sortedBy { it.y }

        return Pair(
            (sortedX.first().x .. sortedX.last().x),
            (0..(floor ?: sortedY.last().y))
        )
    }

    private fun getPrintableStrings(): List<String> {
        val ranges = rangeOfActiveCells()
        return (ranges.second).map { j ->
            ranges.first.map { i ->
                when (this[Position(i,j)]) {
                    CaveContents.ROCK -> '#'
                    CaveContents.AIR -> '.'

                    CaveContents.SAND -> 'o'

                }
            }.joinToString("")
        }.toList()
    }

    fun print() {
        for (line in getPrintableStrings()) println(line)
    }
}

fun simulateFall(grid: CaveGrid, maxDepth: Int?): Boolean {
    var position = Position(500, 0)
    val directions = listOf(
        DirectionWithDiagonals.RIGHT, DirectionWithDiagonals.UP_RIGHT, DirectionWithDiagonals.DOWN_RIGHT
    )
    while (true) {
        if (position.y >= (maxDepth ?:1000)) return true
        position =
            position.moveWithDiagonal(directions.find { grid[position.moveWithDiagonal(it)] == CaveContents.AIR } ?: break)
    }
    grid[position] = CaveContents.SAND
    return false
}

