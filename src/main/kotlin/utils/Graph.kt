package utils

data class GraphEdge(val source: String, val destination: String, val cost: Int)

data class GraphNode<T>(
    val name: String,
    val value: T,
    val edges: List<GraphEdge>,
    val edgesSorted: List<GraphEdge> = edges.sortedBy { it.cost },
    val edgeMap: Map<String, GraphEdge> = edges.associateBy { it.destination },
)

fun <T> getAllDistancesFloydWarshall(nodes: List<GraphNode<T>>): Map<Pair<String, String>, Int> {
    val dist = hashMapOf<Pair<String, String>, Int>()
    val edges = nodes.flatMap { it.edges }
    dist.putAll(edges.associate { Pair(Pair(it.source, it.destination), it.cost) })
    dist.putAll(nodes.associate { Pair(Pair(it.name, it.name), 0) })

    for (k in nodes) {
        for (i in nodes) {
            for (j in nodes) {
                val dist1 = dist[Pair(i.name, k.name)]
                val dist2 = dist[Pair(k.name, j.name)]
                if (dist1 != null && dist2 != null && (dist[Pair(i.name, j.name)] ?: Int.MAX_VALUE) > dist1 + dist2) {
                    dist[Pair(i.name, j.name)] = dist1 + dist2
                }
            }
        }
    }
    return dist.toMap()
}
