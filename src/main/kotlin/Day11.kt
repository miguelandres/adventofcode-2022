import java.io.File
import java.math.BigInteger

data class Monkey(
    val id: Int,
    var items: List<ULong>,
    val operation: (ULong) -> ULong,
    val testValue: ULong,
    val nextMonkeyIfTrue: Int,
    val nextMonkeyIfFalse: Int,
    var countInspections: BigInteger = BigInteger.ZERO
)

fun parseOperation(operator: String, operand: ULong?): (ULong) -> ULong {
    return { x: ULong ->
        val list = listOf(x, operand ?: x)
        if (operator == "+")
            list.reduce { a, b -> a + b }
        else
            list.reduce { a, b -> a * b }
    }
}

fun main() {
    val monkeys =
        File("input/d11p01.txt").readLines().filter { it.isNotEmpty() }.chunked(6).mapIndexed { index, lines ->
            val operation = lines[2].split(" ")
            val operationFn = parseOperation(operation[operation.size - 2], operation.last().toULongOrNull())
            Monkey(
                index + 1,
                lines[1].substringAfter(": ").split(", ").map { it.toULong() },
                operationFn,
                lines[3].split(" ").last().toULong(),
                lines[4].split(" ").last().toInt(),
                lines[5].split(" ").last().toInt()
            )

        }.toList()

    val monkeysRules1 = monkeys.map { it.copy() }
    executeRounds(monkeysRules1, { x: ULong -> x / 3UL }, 20)

    val sortedMonkeys1 = monkeysRules1.sortedBy { it.countInspections }.reversed()
    println(sortedMonkeys1[0].countInspections * sortedMonkeys1[1].countInspections)

    val monkeysRules2 = monkeys.map { it.copy() }

    // It's weird but the "way to manage your worry levels" you have to find does not refer to just how large the numbers
    // will be, but rather find weird shit to minimize the number, in this case it's the modulo based on the minimum
    // common divisor there, it's... weird.... I don't like this problem because there is only one right answer that
    // is badly specified, but whatever
    val mcd = monkeysRules2.map { it.testValue }.reduce { a, b -> a * b }
    val worryReduce2 = { x: ULong -> x % mcd }
    executeRounds(monkeysRules2, worryReduce2, 10_000)

    val sortedMonkeys2 = monkeysRules2.sortedBy { it.countInspections }.reversed()
    println(sortedMonkeys2[0].countInspections * sortedMonkeys2[1].countInspections)
}

private fun executeRounds(
    monkeys: List<Monkey>,
    worryReduceFn: (ULong) -> ULong,
    rounds: Int
) {
    for (x in 1..rounds) {
        monkeys.forEach { m ->
            executeTurn(m, monkeys, worryReduceFn)
        }
    }
}

private fun executeTurn(
    m: Monkey,
    monkeys: List<Monkey>,
    worryReduceFn: (ULong) -> ULong
) {
    while (m.items.isNotEmpty()) {
        var currentItem = m.items.first()
        currentItem = m.operation(currentItem)
        currentItem = worryReduceFn(currentItem)
        m.items = m.items.drop(1)
        m.countInspections++
        val nextMonkeyId = if ((currentItem % m.testValue) == 0UL) {
            m.nextMonkeyIfTrue
        } else {
            m.nextMonkeyIfFalse
        }
        val nextMonkey = monkeys[nextMonkeyId]
        nextMonkey.items = nextMonkey.items.plus(currentItem)
    }
}