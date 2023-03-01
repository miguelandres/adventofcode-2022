import utils.DistanceAwareSparseMap
import utils.Position
import utils.readAocInput
import utils.splitWithPrefix

fun main() {
    processDay15(0, 0..20)
    processDay15(1, 0..4_000_000)
}

private fun processDay15(part: Int, possibleRange: IntRange) {
    println("Processing part $part, with possible range $possibleRange")
    val sensorsAndBeacons = readAocInput(15, part)
        .map {
            val positions = splitWithPrefix(it, "Sensor at ", ": closest beacon is at ").map { posString ->
                parsePositionWithAxisNames(posString, ", ")
            }
            Pair(positions[0], positions[1])
        }

    val map = DistanceAwareSparseMap(BeaconMapContents.UNKNOWN)
    fillMap(sensorsAndBeacons, map)

    println(countEmpties(10, map))
    println(countEmpties(2000000, map))

    val foundBeacon =
        map.copyMapDistanceAwareDefaults().mapValues { it.value.first }.entries
            .flatMap { entry ->
                entry.key.pointsAtManhattanDistance(entry.value + 1).filter {
                    it.inRanges(possibleRange, possibleRange)
                }
            }
            .find { map[it] == BeaconMapContents.UNKNOWN }!!
    println(foundBeacon)
    println(
        foundBeacon.x.toULong() * 4_000_000UL + foundBeacon.y.toULong(),
    )
}

private fun countEmpties(y: Int, map: DistanceAwareSparseMap<BeaconMapContents>) =
    map.rangeX().count { x -> map[Position(x, y)] in sequenceOf(BeaconMapContents.EMPTY, BeaconMapContents.SENSOR) }

private fun fillMap(
    sensorsAndBeacons: List<Pair<Position, Position>>,
    map: DistanceAwareSparseMap<BeaconMapContents>,
) {
    for ((sensor, beacon) in sensorsAndBeacons) {
        val distance = sensor.manhattanDistance(beacon)
        map[sensor] = BeaconMapContents.SENSOR
        map[beacon] = BeaconMapContents.BEACON
        map.addDistanceAwareDefault(sensor, distance, BeaconMapContents.EMPTY)
    }
}

private enum class BeaconMapContents {
    UNKNOWN,
    BEACON,
    SENSOR,
    EMPTY,
}
