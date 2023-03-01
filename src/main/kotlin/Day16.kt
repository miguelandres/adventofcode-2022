import utils.GraphEdge
import utils.GraphNode
import utils.getAllDistancesFloydWarshall
import utils.readAocInput
import utils.wrapInTimeMeasurement
import utils.wrapInTimeMeasurementWithResult

private data class MemoizationPosition(
    val time: Int,
    val location: String,
    val valvesOpen: Set<String>,
)

fun main() {
    val (graph, valvesWithPressure, distances) = wrapInTimeMeasurementWithResult(
        ::parseAndPreProcess,
        "parse and preprocess",
    )
    wrapInTimeMeasurement({ part1(graph, distances, valvesWithPressure) }, "part1")
    wrapInTimeMeasurement({ part2(graph, distances, valvesWithPressure) }, "part2")
}

private fun parseAndPreProcess(): Triple<Map<String, GraphNode<Int>>, Set<String>, Map<Pair<String, String>, Int>> {
    val graph = readAocInput(16, 1).associate {
        val words = it.split(" ")
        val valve = words[1]
        val rate = words[4].dropLast(1).drop(5).toInt()
        Pair(
            valve,
            GraphNode(
                valve,
                rate,
                words.drop(9).map { s -> s.removeSuffix(",") }.map { dest -> GraphEdge(valve, dest, 1) },
            ),
        )
    }

    val valvesWithPressure = graph.entries.map { it.value }.filter { it.value > 0 }.map { it.name }.toSet()
    val distances = getAllDistancesFloydWarshall(graph.values.toList())
    return Triple(graph, valvesWithPressure, distances)
}

private fun part1(
    graph: Map<String, GraphNode<Int>>,
    distances: Map<Pair<String, String>, Int>,
    valvesWithPressure: Set<String>,
) {
    val nextSteps = listOf(Pair(MemoizationPosition(0, "AA", emptySet()), 0))
    val (max, _) = openValves(nextSteps, graph, distances, valvesWithPressure)
    println(max)
}

private fun part2(
    graph: Map<String, GraphNode<Int>>,
    distances: Map<Pair<String, String>, Int>,
    valvesWithPressure: Set<String>,
) {
    val nextSteps = listOf(Pair(MemoizationPosition(4, "AA", emptySet()), 0))
    val (_, solutions) = openValves(nextSteps, graph, distances, valvesWithPressure)
    val entries = solutions.entries.sortedBy { it.value }.reversed()
    var max = Int.MIN_VALUE
    entries.forEach { sol1 ->
        max = max.coerceAtLeast(sol1.value)
        val entriesIterator = entries.iterator()
        while (entriesIterator.hasNext()) {
            val sol2 = entriesIterator.next()
            if (sol2.value + sol1.value <= max) {
                // Entries are sorted by value, which means that values are gonna be smaller from now on.
                break
            }
            if (sol2.key.valvesOpen.intersect(sol1.key.valvesOpen).isEmpty()) {
                max = sol1.value + sol2.value
            }
        }
    }
    println(max)
}

private fun openValves(
    nextSteps: List<Pair<MemoizationPosition, Int>>,
    graph: Map<String, GraphNode<Int>>,
    distances: Map<Pair<String, String>, Int>,
    valvesWithPressure: Set<String>,
): Pair<Int, HashMap<MemoizationPosition, Int>> {
    var nextSteps1 = nextSteps
    val solutions = HashMap<MemoizationPosition, Int>()

    var max = Int.MIN_VALUE

    while (nextSteps1.isNotEmpty()) {
        max = nextSteps1.maxOf { it.second }.coerceAtLeast(max)
        nextSteps1 = nextSteps1.flatMap {
            if ((solutions[it.first] ?: Int.MIN_VALUE) < it.second) {
                solutions[it.first] = it.second
                generateNextSteps(it.first, it.second, graph, distances, valvesWithPressure, solutions)
            } else {
                emptyList()
            }
        }
    }
    return Pair(max, solutions)
}

private fun generateNextSteps(
    pos: MemoizationPosition,
    pressureReleased: Int,
    graph: Map<String, GraphNode<Int>>,
    distances: Map<Pair<String, String>, Int>,
    valvesWithPressure: Set<String>,
    solutions: HashMap<MemoizationPosition, Int>,
): List<Pair<MemoizationPosition, Int>> {
    val moves =
        valvesWithPressure.filter { it !in pos.valvesOpen }.map { destination ->
            Pair(
                pos.copy(time = pos.time + distances[Pair(pos.location, destination)]!!, location = destination),
                pressureReleased,
            )
        }.plus(Pair(pos.copy(time = pos.time + 1), pressureReleased))

    val movesAndOpen = if (pos.location in valvesWithPressure && pos.location !in pos.valvesOpen) {
        moves.plus(
            Pair(
                pos.copy(time = pos.time + 1, valvesOpen = pos.valvesOpen.plus(pos.location)),
                pressureReleased + graph[pos.location]!!.value * (29 - pos.time),
            ),
        )
    } else {
        moves
    }
    return movesAndOpen.filter {
        it.first.time <= 30 && (solutions[it.first] ?: Int.MIN_VALUE) < it.second
    }
}
