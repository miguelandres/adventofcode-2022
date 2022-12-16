import utils.GraphEdge
import utils.GraphNode
import utils.getAllDistancesFloydWarshall
import utils.readAocInput

private data class MemoizationPosition(
    val time: Int,
    val location: String,
    val valvesOpen: Set<String>
)

fun main() {
    val graph = readAocInput(16, 0).associate {
        val words = it.split(" ")
        val valve = words[1]
        val rate = words[4].dropLast(1).drop(5).toInt()
        Pair(
            valve,
            GraphNode(
                valve,
                rate,
                words.drop(9).map { s -> s.removeSuffix(",") }.map { dest -> GraphEdge(valve, dest, 1) }
            )
        )
    }

    val valvesWithPressure = graph.entries.map { it.value }.filter { it.value > 0 }.map { it.name }.toSet()
    val distances = getAllDistancesFloydWarshall(graph.values.toList())
    val solutions = HashMap<MemoizationPosition, Int>()
    var nextSteps = listOf(Pair(MemoizationPosition(0, "AA", emptySet()), 0))

    var max = Int.MIN_VALUE

    while (nextSteps.isNotEmpty()) {
        max = nextSteps.maxOf { it.second }.coerceAtLeast(max)
        nextSteps = nextSteps.flatMap {
            if ((solutions[it.first] ?: Int.MIN_VALUE) < it.second) {
                solutions[it.first] = it.second
                generateNextSteps(it.first, it.second, graph, distances, valvesWithPressure, solutions)
            } else {
                emptyList()
            }
        }
    }
    println(max)
}

private fun generateNextSteps(
    pos: MemoizationPosition,
    pressureReleased: Int,
    graph: Map<String, GraphNode<Int>>,
    distances: Map<Pair<String, String>, Int>,
    valvesWithPressure: Set<String>,
    solutions: HashMap<MemoizationPosition, Int>
): List<Pair<MemoizationPosition, Int>> {
    val moves =
        valvesWithPressure.filter { it !in pos.valvesOpen }.map { destination ->
            Pair(
                pos.copy(time = pos.time + distances[Pair(pos.location, destination)]!!, location = destination),
                pressureReleased
            )
        }.plus(Pair(pos.copy(time = pos.time + 1), pressureReleased))

    val movesAndOpen = if (pos.location in valvesWithPressure && pos.location !in pos.valvesOpen) {
        moves.plus(
            Pair(
                pos.copy(time = pos.time + 1, valvesOpen = pos.valvesOpen.plus(pos.location)),
                pressureReleased + graph[pos.location]!!.value * (29 - pos.time)
            )
        )
    } else {
        moves
    }
    return movesAndOpen.filter {
        it.first.time <= 30 && (solutions[it.first] ?: Int.MIN_VALUE) < it.second
    }
}
