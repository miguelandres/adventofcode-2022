import utils.GraphEdge
import utils.GraphNode
import utils.getAllDistancesFloydWarshall
import utils.readAocInput

private data class MemoizationPosition(
    val time: Int,
    val location: String,
    val bitwiseSet: Int,
    val pressureReleased: Int
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

    val bitwiseMap = graph.entries.map { it.value }.filter { it.value > 0 }
        .mapIndexed { index, graphNode -> Pair(graphNode.name, 1 shl index) }.toMap()
    val reverseBitwiseMap = bitwiseMap.entries.associate { Pair(it.value, it.key) }
    val sumBitwise = bitwiseMap.entries.sumOf { it.value }

    val distances = getAllDistancesFloydWarshall(graph.values.toList())

    val solutions = HashMap<Triple<Int, String, Int>, Int>()
    // solutions[0][mapLocations["AA"]!!][0] = 0

    var nextSteps = listOf(MemoizationPosition(0, "AA", 0, 0))

    var max = Int.MIN_VALUE

    while (nextSteps.isNotEmpty()) {
        max = nextSteps.maxOf { it.pressureReleased }.coerceAtLeast(max)
        nextSteps = nextSteps.flatMap {
            if ((
                solutions[Triple(it.time, it.location, it.bitwiseSet)]
                    ?: Int.MIN_VALUE
                ) < it.pressureReleased
            ) {
                solutions[Triple(it.time, it.location, it.bitwiseSet)] = it.pressureReleased
                generateNextSteps(it, graph, distances, bitwiseMap, solutions)
            } else {
                emptyList()
            }
        }
    }
    println(max)

    val validEndings = solutions.filter { it.key.first == 26 && ((bitwiseMap[it.key.second] ?: 0) and it.key.third != 0) }
    val resultWithHelp = validEndings.maxOf { end1 ->
        validEndings
            .filter { end2 -> (end1.key.third and end2.key.third) == 0 }
            .maxOfOrNull { end2 -> end1.value + end2.value } ?: 0
    }

    println(resultWithHelp)
}

private fun generateNextSteps(
    pos: MemoizationPosition,
    graph: Map<String, GraphNode<Int>>,
    distances: Map<Pair<String, String>, Int>,
    bitwiseMap: Map<String, Int>,
    solutions: HashMap<Triple<Int, String, Int>, Int>
): List<MemoizationPosition> {
    val moves =
        bitwiseMap.entries.filter { entry -> entry.value and pos.bitwiseSet == 0 }.map { it.key }.map { destination ->
            pos.copy(time = pos.time + distances[Pair(pos.location, destination)]!!, location = destination)
        }.plus(pos.copy(time = pos.time + 1))
    val bitwiseLocation = bitwiseMap[pos.location]
    return (
        if (bitwiseLocation != null && pos.bitwiseSet and bitwiseLocation == 0) {
            moves.plus(
                pos.copy(
                    time = pos.time + 1,
                    bitwiseSet = pos.bitwiseSet or bitwiseLocation,
                    pressureReleased = pos.pressureReleased + graph[pos.location]!!.value * (29 - pos.time)
                )
            )
        } else {
            moves
        }
        ).filter {
        it.time <= 30 &&
            (solutions[Triple(it.time, it.location, it.bitwiseSet)] ?: Int.MIN_VALUE) < it.pressureReleased
    }
}
