package day02

import scala.io.Source

enum Hand(val score: Int) {
  case Rock extends Hand(1)
  case Paper extends Hand(2)
  case Scissors extends Hand(3)

  def winsAgainst(otherHand: Hand): Boolean = {
    (this, otherHand) match {
      case (Rock, Scissors)  => true
      case (Scissors, Paper) => true
      case (Paper, Rock)     => true
      case _                 => false
    }
  }
  def losesAgainst(otherHand: Hand): Boolean = {
    (this, otherHand) match {
      case (Scissors, Rock)  => true
      case (Paper, Scissors) => true
      case (Rock, Paper)     => true
      case _                 => false
    }
  }

  def scoreAgainst(otherHand: Hand): Int = {
    this.score + (if (this == otherHand) { 3 }
                  else if (this.losesAgainst(otherHand)) { 0 }
                  else { 6 })

  }

  def winningPlay: Hand = {
    this match {
      case Rock     => Paper
      case Paper    => Scissors
      case Scissors => Rock
    }
  }
  def losingPlay: Hand = {
    this match {
      case Rock     => Scissors
      case Paper    => Rock
      case Scissors => Paper
    }
  }

}

object Hand {
  def apply(s: String): Hand = {
    s match {
      case "A" => Rock
      case "X" => Rock
      case "B" => Paper
      case "Y" => Paper
      case "C" => Scissors
      case "Z" => Scissors
      case _   => ???
    }
  }
}

case class Play(otherPlayer: Hand, you: Hand) {
  def score: (Int, Int) = {
    (otherPlayer.scoreAgainst(you), you.scoreAgainst(otherPlayer))
  }
}

object Play {
  def apply(s: String): Play = {
    val parts = s.split(" ").map(Hand.apply)
    Play(parts(0), parts(1))
  }
}

object PlayStrategy {
  def apply(s: String): Play = {
    val parts = s.split(" ")
    val otherHand = Hand(parts(0))
    val yourHand = parts(1) match {
      case "X" => otherHand.losingPlay
      case "Y" => otherHand
      case "Z" => otherHand.winningPlay
    }
    Play(otherHand, yourHand)
  }
}

object JanKenPon extends App {

  var plays = Source.fromFile("input/d02p01.txt").getLines().map(Play.apply)
  var sum = plays.map(_.score).map { case (_, you) => you }.sum
  println(sum)

  plays = Source.fromFile("input/d02p01.txt").getLines().map(PlayStrategy.apply)
  sum = plays.map(_.score).map { case (_, you) => you }.sum
  println(sum)
}
