package hellotest

import scala.collection.mutable.Stack

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.Suite
import org.scalatest.matchers.must.Matchers.*
import org.scalatest.matchers.should.Matchers
import scala.io.Source
import Main.run

class WordCloudTest extends AnyFlatSpec with Matchers {

  "A word cloud with a number of the same word in it equal to the windowSize" should
    "have a topWords list that has that word's frequency as windowSize" in {
    TestLogger.clear() // Reset logs
    val sut = new CircularQueue(6)
    val words = Iterator("asking",
      "asking",
      "asking",
      "asking",
      "asking",
      "asking")
    Main.processWords(sut, words, 1, 1, 1, 6, 1)
    TestLogger.logs should contain("Top words: List((asking,6))")
  }

  "A word cloud where no words go above the minFreq" should "have an empty topWords list" in {
    TestLogger.clear() // Reset logs
    val sut = new CircularQueue(6)
    val words = Iterator("asking",
      "time",
      "free",
      "wall",
      "hear",
      "nine")
    Main.processWords(sut, words, 1, 1, 1, 1, 2)
    TestLogger.logs should contain("Top words: List()")
  }

  "A word cloud where no words go above the minLength" should "have an empty topWords list" in {
    TestLogger.clear() // Reset logs
    val sut = new CircularQueue(6)
    val words = Iterator("asking",
      "time",
      "free",
      "wall",
      "hear",
      "nine")
    Main.processWords(sut, words, 7, 1, 1, 1, 1)
    TestLogger.logs.isEmpty should be (true)
  }
  "A word cloud where the number of words is less than cloudSize" should "have an empty topWords list" in {
    TestLogger.clear() // Reset logs
    val sut = new CircularQueue(6)
    val words = Iterator("asking",
      "time",
      "free",
      "wall",
      "hear",
      "nine")
    Main.processWords(sut, words, 1, 10, 1, 1, 1)
    TestLogger.logs should contain("Top words: List()")
  }
  "A word cloud where with fewer words than the minimum number of steps" should "not have printed" in {
    TestLogger.clear() // Reset logs
    val sut = new CircularQueue(6)
    val words = Iterator("asking",
      "time",
      "free",
      "wall",
      "hear",
      "nine")
    Main.processWords(sut, words, 1, 1, 10, 1, 1)
    TestLogger.logs.isEmpty should be(true)
  }

}