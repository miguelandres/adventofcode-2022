import utils.readAocInput

fun main() {
    val lines = readAocInput(1).map { s -> s.toIntOrNull() }
        .fold(listOf(mutableListOf())) { elves: List<MutableList<Int>>, maybeCalories: Int? ->
            if (maybeCalories == null) {
                elves.plusElement(mutableListOf())
            } else {
                elves.last().add(maybeCalories)
                elves
            }
        }
    val sums = lines.map { s -> s.sum() }.sorted().reversed()
    println(sums[0])
    println(sums.take(3).fold(0) { x, y -> y + x })
}