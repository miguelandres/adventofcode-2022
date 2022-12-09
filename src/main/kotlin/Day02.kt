import java.io.File

enum class Hand(private val score: Int) {
    ROCK(1) {
        override fun winningHand(): Hand =
            PAPER


        override fun losingHand(): Hand =
            SCISSORS

    },
    PAPER(2) {
        override fun winningHand(): Hand = SCISSORS


        override fun losingHand(): Hand =
            ROCK

    },
    SCISSORS(3) {
        override fun winningHand(): Hand =
            ROCK


        override fun losingHand(): Hand =
            PAPER

    };

    abstract fun winningHand(): Hand
    abstract fun losingHand(): Hand
    private fun losesAgainst(other: Hand): Boolean {
        return other == winningHand()
    }

    private fun winsAgainst(other: Hand): Boolean {
        return other == losingHand()
    }

    fun scoreAgainst(other: Hand): Int {
        return score + if (losesAgainst(other)) {
            0
        } else if (winsAgainst(other)) {
            6
        } else {
            3
        }
    }

    fun applyStrategy(strategy: String): Hand {
        return when (strategy) {
            "X" -> losingHand()
            "Y" -> this
            "Z" -> winningHand()
            else -> this
        }
    }
}


fun parseHand(s: String): Hand {
    return when (s) {
        "A" -> Hand.ROCK
        "B" -> Hand.PAPER
        "C" -> Hand.SCISSORS
        "X" -> Hand.ROCK
        "Y" -> Hand.PAPER
        "Z" -> Hand.SCISSORS
        else -> Hand.ROCK
    }
}

fun main() {
    val lines = File("input/d02p01.txt").readLines().map { s -> s.split(" ") }

    val plays = lines.map { parts ->
        Pair(parseHand(parts[0]), parseHand(parts[1]))
    }

    val playStrategies = lines.map { parts ->
        val otherHand = parseHand(parts[0])
        val yourHand = otherHand.applyStrategy(parts[1])
        Pair(otherHand, yourHand)
    }

    val playScores = plays.map { play -> play.second.scoreAgainst(play.first) }
    val strategyScores = playStrategies.map { play -> play.second.scoreAgainst(play.first) }

    println(playScores.sum())
    println(strategyScores.sum())
}