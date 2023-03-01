import utils.Direction
import utils.Position
import utils.readAocInput
import utils.wrapInTimeMeasurement
import utils.wrapInTimeMeasurementWithResult

data class Shape(val shape: Set<Position>, val height: Int, val width: Int) {
    companion object {
        fun createShape(positions: Set<Position>): Shape {
            val width = positions.maxOf { it.x + 1 }
            val height = positions.maxOf { it.y + 1 }
            return Shape(positions, height, width)
        }
    }

    override fun toString(): String {
        return (0 until height).joinToString("\n") { y ->
            (0 until width).joinToString("") { x ->
                if (shape.contains(Position(x, height - y - 1))) "#" else "."
            }
        }
    }
}

data class Rock(val shape: Shape, var currentPosition: Position) {
    fun canMoveInDirection(dir: Direction, fallenRocks: HashSet<Position>): Boolean{
        val nextPosition = currentPosition + dir
        if (!nextPosition.inRanges(0..(7 - shape.width), 0..Int.MAX_VALUE)) return false
        return shape.shape.all { !fallenRocks.contains(it + nextPosition) }
    }
}

val shapes: List<Shape> = listOf(
    (0..3).map { Position(it, 0) }.toSet(),
    (0..2).map { Position(1, it) }.toSet().plus((0..2).map { Position(it, 1) }),
    (0..2).map { Position(2, it) }.toSet().plus((0..2).map { Position(it, 0) }),
    (0..3).map { Position(0, it) }.toSet(),
    (0..1).flatMap { x -> (0..1).map {y -> Position(x, y) }}.toSet(),
).map(Shape::createShape)

fun main() {
    shapes.forEachIndexed {
            i, shape ->
        println("Shape number ${i + 1}\n\n$shape")
    }
    val jetStreams = wrapInTimeMeasurementWithResult({
        readAocInput(17, 0).first().map {
            when (it) {
                '>' -> Direction.RIGHT
                else -> Direction.LEFT
            }
        }
    }, "Parse")

    wrapInTimeMeasurement({part1(jetStreams, 2022)}, "part1")
}

private fun part1(jetStreams: CircularList<Direction>, numberOfRocks: Int) {
    val fallenRocks = HashSet<Position>()
    var currentFloor = 0
    var jetIndex = 0
    for (i in (0 until numberOfRocks)) {
        currentFloor = currentFloor.coerceAtLeast(
            simulateRockFall(
                Rock(shapes[i % 5], Position(2, currentFloor + 3)),
                fallenRocks,
                jetStreams[i % jetStreams.size],
            ),
        )
    }
    println(currentFloor)
}

private fun simulateRockFall(rock: Rock, fallenRocks: HashSet<Position>, jetStreams: List<Direction>): Int {
    while (true) {
        // try to move with the jetStreams, if it can't just continue anyway
        if (rock.canMoveInDirection(dir, fallenRocks)) {
            rock.currentPosition += dir
        }
        if (rock.canMoveInDirection(Direction.DOWN, fallenRocks)) {
            rock.currentPosition += Direction.DOWN
        } else {
            break
        }
    }
    fallenRocks.addAll(rock.shape.shape.map{it + rock.currentPosition})
    return rock.currentPosition.y + rock.shape.height
}
