package day01

import scala.io.Source

object Calories extends App {
  val lines = Source
    .fromFile("input/d01p01.txt")
    .getLines
    .map(s => s.toIntOption)
    .foldLeft(Seq(Seq[Int]())) {
      case (elves, Some(cal)) => {
        val tail = elves.tail
        val head = elves.head
        (head :+ cal) +: tail
      }
      case (elves, None) => {
        Seq() +: elves
      }
    }
    .reverse

  val sums = lines.map(_.sum).sorted.reverse
  println(sums(0))

  println(sums.take(3).fold(0) { case (x, y) => x + y })
}
