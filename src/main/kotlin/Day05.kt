import utils.readAocInput

private fun executeOldInstruction(instruction: Triple<Int, Int, Int>, stacks: Array<MutableList<Char>>) {
    for (i in 1..instruction.first) {
        stacks[instruction.third].add(stacks[instruction.second].removeLast())
    }
}

private fun executeNewInstruction(instruction: Triple<Int, Int, Int>, stacks: Array<MutableList<Char>>) {
    val tempList = mutableListOf<Char>()
    for (i in 1..instruction.first) {
        tempList.add(0, stacks[instruction.second].removeLast())
    }
    stacks[instruction.third].addAll(tempList)
}

fun main() {
    val lines = readAocInput(5)
    val breakIndex = lines.indexOf("")

    val stackLines = lines.take(breakIndex).reversed()
    val instructionLines = lines.subList(breakIndex + 1, lines.size)

    val stacks = parseStacksFromLines(stackLines)
    val stacks2 = parseStacksFromLines(stackLines)

    val instructions = instructionLines.map {
        val numbers = it.split(" ").mapNotNull { n -> n.toIntOrNull() }
        Triple(numbers[0], numbers[1] - 1, numbers[2] - 1)
    }

    instructions.forEach { instruction -> executeOldInstruction(instruction, stacks) }
    println(generateTopOfStackString(stacks))

    instructions.forEach { instruction -> executeNewInstruction(instruction, stacks2) }
    println(generateTopOfStackString(stacks2))
}

private fun generateTopOfStackString(stacks: Array<MutableList<Char>>) = stacks.map {
    if (it.isEmpty()) {
        ' '
    } else {
        it.last()
    }
}.joinToString("")

private fun parseStacksFromLines(stackLines: List<String>): Array<MutableList<Char>> {
    val numberOfStacks = stackLines[0].split(" ").mapNotNull { s -> s.toIntOrNull() }.count()

    val stacks = Array(numberOfStacks) { _ -> mutableListOf<Char>() }
    stackLines.drop(1).forEach { line ->
        for (i in 0 until numberOfStacks) {
            val pos = 1 + i * 4
            if (line.length > pos && line[pos] in 'A'..'Z') {
                stacks[i].add(line[pos])
            }
        }
    }
    return stacks
}


