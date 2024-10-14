package hellotest

import scala.collection.mutable.Stack

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.Suite
import org.scalatest.matchers.must.Matchers.*
import org.scalatest.matchers.should.Matchers


class WordCloudTest extends AnyFlatSpec with Matchers {

  "A word cloud with only 1 unique word" should "have a topFrequentWords seq with only 1 element" in {
    val sut = new CircularQueue(6)
    val words = Iterator("asking",
      "asking",
      "asking",
      "asking",
      "asking",
      "asking")
    Main.processWords(sut, words, 6, 6, 1, 6, 6)
  }

}