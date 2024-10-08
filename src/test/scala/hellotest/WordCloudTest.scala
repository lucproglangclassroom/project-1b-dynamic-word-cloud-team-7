package hellotest

import scala.collection.mutable.Stack

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.Suite
import org.scalatest.matchers.must.Matchers.*
import org.scalatest.matchers.should.Matchers


class WordCloudTest extends AnyFlatSpec with Matchers {

  "A word cloud with only 1 unique word" should "have a topFrequentWords seq with only 1 element" in {
    val sut = new CircularQueue(6)
    for(a <- 0 to 6) {
      sut.add(List("asking"))

    }
    assert(sut.topFrequentWords(6, 1).length == 1)
  }

  "A word cloud with 0 words" should "have a topFrequentWords seq with 0 elements" in {
    val sut = new CircularQueue(6)
    assert(sut.topFrequentWords(6, 1).isEmpty)
  }

  "A word cloud with only unique words and a minFrequency greater than 1" should "have a topFrequentWords seq with 0 elements" in {
    val sut = new CircularQueue(6)
    for (a <- 0 to 6) {
      sut.add(List(a.toString))

    }
    assert(sut.topFrequentWords(6, 2).isEmpty)
  }
}